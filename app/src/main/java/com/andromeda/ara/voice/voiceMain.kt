@file:Suppress("SENSELESS_COMPARISON")

package com.andromeda.ara.voice

import android.content.Context
import android.content.res.AssetManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.andromeda.ara.RecognizeCommands
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import java.util.concurrent.locks.ReentrantLock

public class voiceMain {
    private val REQUEST_LOCATION_PERMISSION = 1
    private val SAMPLE_RATE = 16000
    private val SAMPLE_DURATION_MS = 1000
    private val RECORDING_LENGTH = SAMPLE_RATE * SAMPLE_DURATION_MS / 1000
    private val AVERAGE_WINDOW_DURATION_MS: Long = 500
    private val DETECTION_THRESHOLD = 0.70f
    private val SUPPRESSION_MS = 1500
    private var tfLite: Interpreter? = null
    internal var resulttxt: String? = "err"
    private val MINIMUM_COUNT = 3
    private val MINIMUM_TIME_BETWEEN_SAMPLES_MS: Long = 30
    private val LABEL_FILENAME = "file:///android_asset/conv_actions_labels.txt"
    private val MODEL_FILENAME = "file:///android_asset/conv_actions_frozen.tflite"
    private val INPUT_DATA_NAME = "decoded_sample_data:0"
    private val SAMPLE_RATE_NAME = "decoded_sample_data:1"
    private val OUTPUT_SCORES_NAME = "labels_softmax"
    private var recordingThread: Thread? = null
    internal var shouldContinueRecognition = true
    private var recognitionThread: Thread? = null
    private val recordingBufferLock = ReentrantLock()
    private var recognizeCommands: RecognizeCommands? = null
    internal var recordingBuffer = ShortArray(RECORDING_LENGTH)
    internal var recordingOffset = 0
    internal var shouldContinue = true
    private val labels = ArrayList<String>()
    private val displayedLabels = ArrayList<String>()
    private val LOG_TAG = "e"
    @Throws(IOException::class)
    private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @Synchronized
    fun startRecognition() {
        if (recognitionThread != null) {
            return
        }
        shouldContinueRecognition = true
        recognitionThread = Thread(
                Runnable { recognize() })
        recognitionThread!!.start()
    }

    @Synchronized
    fun stopRecognition() {
        if (recognitionThread == null) {
            return
        }
        shouldContinueRecognition = false
        recognitionThread = null
    }

    @Synchronized
    fun startRecording() {
        if (recordingThread != null) {
            return
        }
        shouldContinue = true
        recordingThread = Thread(
                Runnable { record() })
        recordingThread!!.start()
    }

    private fun record() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO)

        // Estimate the buffer size we'll need for this device.
        var bufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2
        }
        val audioBuffer = ShortArray(bufferSize / 2)

        val record = AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize)

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!")
            return
        }

        record.startRecording()

        Log.v(LOG_TAG, "Start recording")

        // Loop, gathering audio data and copying it to a round-robin buffer.
        while (shouldContinue) {
            val numberRead = record.read(audioBuffer, 0, audioBuffer.size)
            val maxLength = recordingBuffer.size
            val newRecordingOffset = recordingOffset + numberRead
            val secondCopyLength = Math.max(0, newRecordingOffset - maxLength)
            val firstCopyLength = numberRead - secondCopyLength
            // We store off all the data for the recognition thread to access. The ML
            // thread will copy out of this buffer into its own, while holding the
            // lock, so this should be thread safe.
            recordingBufferLock.lock()
            try {
                System.arraycopy(audioBuffer, 0, recordingBuffer, recordingOffset, firstCopyLength)
                System.arraycopy(audioBuffer, firstCopyLength, recordingBuffer, 0, secondCopyLength)
                recordingOffset = newRecordingOffset % maxLength
            } finally {
                recordingBufferLock.unlock()
            }
            stopRecognition()
        }

        record.stop()
        record.release()
    }

    @Synchronized
    fun stopRecording() {
        if (recordingThread == null) {
            return
        }
        shouldContinue = false
        recordingThread = null
    }

    private fun recognize() {

        Log.v(LOG_TAG, "Start recognition")

        val inputBuffer = ShortArray(RECORDING_LENGTH)
        val floatInputBuffer = Array(RECORDING_LENGTH) { FloatArray(1) }
        val outputScores = Array(1) { FloatArray(labels.size) }
        val sampleRateList = intArrayOf(SAMPLE_RATE)

        // Loop, grabbing recorded data and running the recognition model on it.
        while (shouldContinueRecognition) {
            val startTime = Date().time
            // The recording thread places data in this round-robin buffer, so lock to
            // make sure there's no writing happening and then copy it to our own
            // local version.
            recordingBufferLock.lock()
            try {
                val maxLength = recordingBuffer.size
                val firstCopyLength = maxLength - recordingOffset
                val secondCopyLength = recordingOffset
                System.arraycopy(recordingBuffer, recordingOffset, inputBuffer, 0, firstCopyLength)
                System.arraycopy(recordingBuffer, 0, inputBuffer, firstCopyLength, secondCopyLength)
            } finally {
                recordingBufferLock.unlock()
            }

            // We need to feed in float values between -1.0f and 1.0f, so divide the
            // signed 16-bit inputs.
            for (i in 0 until RECORDING_LENGTH) {
                floatInputBuffer[i][0] = inputBuffer[i] / 32767.0f
            }

            val inputArray = arrayOf<Any>(floatInputBuffer, sampleRateList)
            val outputMap = HashMap<Int, Any>()
            outputMap[0] = outputScores

            // Run the model.
            tfLite?.runForMultipleInputsOutputs(inputArray, outputMap)

            // Use the smoother to figure out if we've had a real recognition event.
            val currentTime = System.currentTimeMillis()
            val result = recognizeCommands?.processLatestResults(outputScores[0], currentTime)
            // lastProcessingTimeMs = new Date().getTime() - startTime;
            //runOnUiThread(
            //      Runnable {
            //inferenceTimeTextView.setText(lastProcessingTimeMs + " ms");

            // If we do have a new command, highlight the right list entry.
            if (result?.foundCommand?.startsWith("_")!! && result!!.isNewCommand) {
                var labelIndex = -1
                for (i in labels.indices) {
                    if (labels.get(i) == result.foundCommand) {
                        labelIndex = i
                    }
                }
                when (labelIndex - 2) {
                    0 -> resulttxt = "yes"
                    1 -> resulttxt = "no"
                    2 -> resulttxt = "up"
                    3 -> resulttxt = "down"
                    4 -> resulttxt = "left"
                    5 -> resulttxt = "right"
                    6 -> resulttxt = "on"
                    7 -> resulttxt = "off"
                    8 -> resulttxt = "stop"
                    9 -> resulttxt = "go"
                }


            }
            // })
            try {
                // We don't need to run too frequently, so snooze for a bit.
                Thread.sleep(MINIMUM_TIME_BETWEEN_SAMPLES_MS)
            } catch (e: InterruptedException) {
                // Ignore
            }

        }

        Log.v(LOG_TAG, "End recognition")
        if (resulttxt == null) {
            resulttxt = "err"

        }
    }

    fun start(ctx: Context): String? {
        var main: String = "err"
        val actualLabelFilename = LABEL_FILENAME.split("file:///android_asset/".toRegex()).toTypedArray()[1]
        Log.i(LOG_TAG, "Reading labels from: $actualLabelFilename")
        var br: BufferedReader? = null
        try {
            br = BufferedReader(InputStreamReader(ctx.assets.open(actualLabelFilename)))
            var line: String? = null
            while (line == br.readLine() != null) {
                line?.let { labels.add(it) }
                if (line?.get(0) != '_') {
                    displayedLabels.add(line?.substring(0, 1)?.toUpperCase() + line?.substring(1))
                }
            }
            br.close()
        } catch (e: IOException) {
            throw RuntimeException("Problem reading label file!", e)
        }


        // Set up an object to smooth recognition results to increase accuracy.
        recognizeCommands = RecognizeCommands(
                labels,
                AVERAGE_WINDOW_DURATION_MS,
                DETECTION_THRESHOLD,
                SUPPRESSION_MS,
                MINIMUM_COUNT,
                MINIMUM_TIME_BETWEEN_SAMPLES_MS)

        val actualModelFilename = MODEL_FILENAME.split("file:///android_asset/".toRegex()).toTypedArray()[1]
        try {
            tfLite = Interpreter(loadModelFile(ctx.getAssets(), actualModelFilename))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }


        tfLite!!.resizeInput(0, intArrayOf(RECORDING_LENGTH, 1))
        tfLite!!.resizeInput(1, intArrayOf(1))

        // Start the recording and recognition threads.
        //requestMicrophonePermission()
        startRecording()
        startRecognition()
        return resulttxt
    }
}