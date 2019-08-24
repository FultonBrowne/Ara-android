/*
 * Copyright (c) 2019. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.voice;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import com.andromeda.ara.MainActivity;
import com.andromeda.ara.RecognizeCommands;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class run {
    private List<String> displayedLabels = new ArrayList<>();
    private String resulttxt;
    private RecognizeCommands recognizeCommands = null;
    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_DURATION_MS = 1000;
    private static final int RECORDING_LENGTH = SAMPLE_RATE * SAMPLE_DURATION_MS / 1000;
    private static final long AVERAGE_WINDOW_DURATION_MS = 1000;
    private static final float DETECTION_THRESHOLD = 0.50f;
    private static final int SUPPRESSION_MS = 1500;
    private static final int MINIMUM_COUNT = 3;
    private Thread recordingThread;
    private boolean shouldContinueRecognition = true;
    private Activity act1;
    private Thread recognitionThread;
    private boolean shouldContinue = true;
    private short[] recordingBuffer = new short[RECORDING_LENGTH];
    private final ReentrantLock recordingBufferLock = new ReentrantLock();

    private int recordingOffset = 0;

    private static final long MINIMUM_TIME_BETWEEN_SAMPLES_MS = 30;
    private static final String LABEL_FILENAME = "file:///android_asset/conv_actions_labels.txt";
    private static final String MODEL_FILENAME = "file:///android_asset/conv_actions_frozen.tflite";

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<String> labels = new ArrayList<>();

    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
            throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private Interpreter tfLite;

    public synchronized String run1(Context ctx, Activity act) {
        act1 = act;
        String actualLabelFilename = LABEL_FILENAME.split("file:///android_asset/", -1)[1];
        Log.i(LOG_TAG, "Reading labels from: " + actualLabelFilename);
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(actualLabelFilename)));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
                if (line.charAt(0) != '_') {
                    displayedLabels.add(line.substring(0, 1).toUpperCase() + line.substring(1));
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading label file!", e);
        }

        // Set up an object to smooth recognition results to increase accuracy.
        recognizeCommands =
                new RecognizeCommands(
                        labels,
                        AVERAGE_WINDOW_DURATION_MS,
                        DETECTION_THRESHOLD,
                        SUPPRESSION_MS,
                        MINIMUM_COUNT,
                        MINIMUM_TIME_BETWEEN_SAMPLES_MS);

        String actualModelFilename = MODEL_FILENAME.split("file:///android_asset/", -1)[1];
        try {
            tfLite = new Interpreter(loadModelFile(ctx.getAssets(), actualModelFilename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        tfLite.resizeInput(0, new int[]{RECORDING_LENGTH, 1});
        tfLite.resizeInput(1, new int[]{1});
        startRecording();
        startRecognition();
        //return resulttxt;
        if (resulttxt == null) resulttxt = "err";
        return resulttxt;
    }

    public synchronized void startRecognition() {
        if (recognitionThread != null) {
            return;
        }
        shouldContinueRecognition = true;
        recognitionThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                recognize();
                            }
                        });
        recognitionThread.start();
    }

    public synchronized void stopRecognition() {
        if (recognitionThread == null) {
            return;
        }
        shouldContinueRecognition = false;
        recognitionThread = null;
    }

    public synchronized void startRecording() {
        if (recordingThread != null) {
            return;
        }
        shouldContinue = true;
        recordingThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                record();
                            }
                        });
        recordingThread.start();
    }

    private void record() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        // Estimate the buffer size we'll need for this device.
        int bufferSize =
                AudioRecord.getMinBufferSize(
                        SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2;
        }
        short[] audioBuffer = new short[bufferSize / 2];

        AudioRecord record =
                new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!");
            return;
        }

        record.startRecording();

        Log.v(LOG_TAG, "Start recording");

        // Loop, gathering audio data and copying it to a round-robin buffer.
        while (shouldContinue) {
            int numberRead = record.read(audioBuffer, 0, audioBuffer.length);
            int maxLength = recordingBuffer.length;
            int newRecordingOffset = recordingOffset + numberRead;
            int secondCopyLength = Math.max(0, newRecordingOffset - maxLength);
            int firstCopyLength = numberRead - secondCopyLength;
            // We store off all the data for the recognition thread to access. The ML
            // thread will copy out of this buffer into its own, while holding the
            // lock, so this should be thread safe.
            recordingBufferLock.lock();
            try {
                System.arraycopy(audioBuffer, 0, recordingBuffer, recordingOffset, firstCopyLength);
                System.arraycopy(audioBuffer, firstCopyLength, recordingBuffer, 0, secondCopyLength);
                recordingOffset = newRecordingOffset % maxLength;
            } finally {
                recordingBufferLock.unlock();
            }
            stopRecording();
        }

        record.stop();
        record.release();
    }

    public synchronized void stopRecording() {
        if (recordingThread == null) {
            return;
        }
        shouldContinue = false;
        recordingThread = null;
    }

    private void recognize() {

        Log.v(LOG_TAG, "Start recognition");

        short[] inputBuffer = new short[RECORDING_LENGTH];
        float[][] floatInputBuffer = new float[RECORDING_LENGTH][1];
        float[][] outputScores = new float[1][labels.size()];
        int[] sampleRateList = new int[]{SAMPLE_RATE};

        // Loop, grabbing recorded data and running the recognition model on it.
        while (shouldContinueRecognition) {
            long startTime = new Date().getTime();
            // The recording thread places data in this round-robin buffer, so lock to
            // make sure there's no writing happening and then copy it to our own
            // local version.
            recordingBufferLock.lock();
            try {
                int maxLength = recordingBuffer.length;
                int firstCopyLength = maxLength - recordingOffset;
                int secondCopyLength = recordingOffset;
                System.arraycopy(recordingBuffer, recordingOffset, inputBuffer, 0, firstCopyLength);
                System.arraycopy(recordingBuffer, 0, inputBuffer, firstCopyLength, secondCopyLength);
            } finally {
                recordingBufferLock.unlock();
            }

            // We need to feed in float values between -1.0f and 1.0f, so divide the
            // signed 16-bit inputs.
            for (int i = 0; i < RECORDING_LENGTH; ++i) {
                floatInputBuffer[i][0] = inputBuffer[i] / 32767.0f;
            }

            Object[] inputArray = {floatInputBuffer, sampleRateList};
            Map<Integer, Object> outputMap = new HashMap<>();
            outputMap.put(0, outputScores);

            // Run the model.
            tfLite.runForMultipleInputsOutputs(inputArray, outputMap);

            // Use the smoother to figure out if we've had a real recognition event.
            long currentTime = System.currentTimeMillis();
            final RecognizeCommands.RecognitionResult result =
                    recognizeCommands.processLatestResults(outputScores[0], currentTime);
            // lastProcessingTimeMs = new Date().getTime() - startTime;
            act1.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {

                            //inferenceTimeTextView.setText(lastProcessingTimeMs + " ms");

                            // If we do have a new command, highlight the right list entry.
                            if (!result.foundCommand.startsWith("_") && result.isNewCommand) {
                                int labelIndex = -1;
                                for (int i = 0; i < labels.size(); ++i) {
                                    if (labels.get(i).equals(result.foundCommand)) {
                                        labelIndex = i;
                                    }
                                }
                                switch (labelIndex - 2) {
                                    case 0:
                                        resulttxt = "yes";

                                        break;
                                    case 1:
                                        resulttxt = "no";
                                        break;
                                    case 2:
                                        resulttxt = "up";

                                        break;
                                    case 3:
                                        resulttxt = "down";
                                        break;
                                    case 4:
                                        resulttxt = "left";
                                        break;
                                    case 5:
                                        resulttxt = "right";
                                        break;
                                    case 6:
                                        resulttxt = "on";
                                        break;
                                    case 7:
                                        resulttxt = "off";
                                        break;
                                    case 8:
                                        resulttxt = "stop";
                                        break;
                                    case 9:
                                        resulttxt = "go";
                                        break;
                                }


                            } else resulttxt = "Fulton";

                        }
                    });
            try {
                // We don't need to run too frequently, so snooze for a bit.
                Thread.sleep(MINIMUM_TIME_BETWEEN_SAMPLES_MS);
            } catch (InterruptedException e) {
                // Ignore
            }
            stopRecognition();
        }

        Log.v(LOG_TAG, "End recognition");
        if (resulttxt == null) {
            resulttxt = "err";

        }
    }

}
