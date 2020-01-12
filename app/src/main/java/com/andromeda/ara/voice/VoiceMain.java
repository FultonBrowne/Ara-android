/*
 * Copyright (c) 2020. Fulton Browne
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

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.*;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andromeda.ara.R;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.RssFeedModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static com.andromeda.ara.constants.ConstantUtils.*;
import static com.andromeda.ara.util.VoiceMainUtils.*;

public class VoiceMain extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {
    private FileOutputStream os = null;
    private Thread recordingThread;
    boolean isRecording;
    ImageView imageView;
    SpellCheckerSession mScs;
    TextServicesManager tsm;
    Boolean blankRunning = false;
    public Context ctx = this;

    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);

    private byte[] Data = new byte[bufferSizeInBytes];
    RecyclerView recyclerView;

    private AudioRecord audioRecorder = new AudioRecord(AUDIO_SOURCE,
            SAMPLE_RATE_HZ,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSizeInBytes);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tsm = (TextServicesManager) getSystemService(
                Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = Objects.requireNonNull(tsm).newSpellCheckerSession(null, null, this, true);
        System.out.println(bufferSizeInBytes);
        setContentView(R.layout.activity_voice_main);


        recyclerView = findViewById(R.id.listVoice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(Collections.singletonList(new RssFeedModel("hello", "how can I help", "", "", "", true)));
        recyclerView.setAdapter(adapter);
        requestMicrophonePermission();


        super.onCreate(savedInstanceState);
        startRecording();
        runTransition();

    }

    void runTransition() {

        runOnUiThread(() -> {
            imageView = findViewById(R.id.imageView);
            imageView.setVisibility(View.VISIBLE);

            AnimationDrawable transition = (AnimationDrawable) imageView.getBackground();
            transition.setEnterFadeDuration(5000);

            // setting exit fade animation duration to 2 seconds
            transition.setExitFadeDuration(2000);
            transition.run();

        });
    }


    public void back(View view) {
        if (isRecording) {
                stopRecording();
                FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
                fab2.setVisibility(View.VISIBLE);

        } else onBackPressed();
    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    private synchronized void startRecording() {
        final MediaPlayer mp = new MediaPlayer();
        mp.reset();
        AssetFileDescriptor afd;
        try{
            afd = getAssets().openFd("start.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        audioRecorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(() -> {
            try {
                new File(getCacheDir(), "record.pcm");
                os = new FileOutputStream(getCacheDir() + "/record.pcm");

                while (isRecording) {
                    audioRecorder.read(Data, 0, getRawDataLength(Data));
                    System.out.println(Data[0]);

                    if (Data[0] == 0) {
                        System.out.println("blank");
                        blankRunning = false;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (!blankRunning) {
                                        stopRecording();
                                        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
                                        fab2.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }, 4000);
                    } else blankRunning = true;
                    try {
                        os.write(Data, 0, bufferSizeInBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                stopAnimation();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
    }

    private void stopRecording() {
        final MediaPlayer mp = new MediaPlayer();
        mp.reset();
        AssetFileDescriptor afd;
        try{
            afd = getAssets().openFd("end.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        if (null != audioRecorder) {
            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            recordingThread = null;
            final String[] phrase = new String[1];
            Thread recognize = new Thread(() -> {
                try {
                    copyAssets();
                    rawToWave(new File(getCacheDir() + "/record.pcm"), new File(getCacheDir() + "/record.wav"));
                    phrase[0] = new DeepSpeech().run(getCacheDir() + "/record.wav", this.getApplicationContext());
                    mScs = tsm.newSpellCheckerSession(null, null, this, true);
                    //mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo(phrase[0])}, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<RssFeedModel> rssFeedModels = new ArrayList<>(new Search().main(phrase[0], getApplicationContext(), VoiceMain.this));

                runOnUiThread(() -> recyclerView.setAdapter(new Adapter(rssFeedModels)));
                try {
                    new TTS().start(getApplicationContext(), rssFeedModels.get(0).out);
                } catch (Exception ignored) {

                }
            });
            recognize.setPriority(Thread.MAX_PRIORITY);
            recognize.start();
            System.out.println("result =" + phrase[0]);
        }
    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];

        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(waveFile))) {
            createWaveHeader(rawData, output);
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[getRawDataLength(rawData) / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }
            output.write(fullyReadFileToBytes(rawFile));
        }
    }

    /**
     * // WAVE header
     * // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
     */
    private void createWaveHeader(byte[] rawData, DataOutputStream output) throws IOException {
        writeString(output, CHUNK_ID);
        writeInt(output, CHUNK_SIZE + getRawDataLength(rawData));
        writeString(output, FORMAT);
        writeString(output, SUB_CHUNK_ID_1);
        writeInt(output, SUB_CHUNK__SIZE_1);
        writeShort(output, AUDIO_FORMAT_PCM);
        writeShort(output, NUMBER_OF_CHANNELS);
        writeInt(output, SAMPLE_RATE_HZ);
        writeInt(output, BYTE_RATE);
        writeShort(output, BLOCK_ALIGN);
        writeShort(output, BITS_PER_SAMPLE);
        writeString(output, SUB_CHUNK_ID_2);
        writeInt(output, getRawDataLength(rawData)); // subchunk 2 size
    }

    private int getRawDataLength(byte[] rawData) {
        return rawData.length;
    }

    byte[] fullyReadFileToBytes(File f) {
        int size = (int) f.length();
        byte[] bytes = new byte[size];
        byte[] tmpBuff = new byte[size];
        try (FileInputStream fis = new FileInputStream(f)) {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getCacheDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                System.out.println(filename);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void stopAnimation() {
        runOnUiThread(() -> imageView.setVisibility(View.INVISIBLE));
    }

    public void exit(View view) {
        onBackPressed();
    }

    public void record(View view) {
        audioRecorder = new AudioRecord(AUDIO_SOURCE,
                SAMPLE_RATE_HZ,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSizeInBytes);
        runTransition();
        startRecording();
    }


    public void onGetSuggestions(final SuggestionsInfo[] arg0) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = arg0[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append("," + arg0[i].getSuggestionAt(j));
            }

            sb.append(" (" + len + ")");
        }

        runOnUiThread(new Runnable() {
            public void run() {
Toast.makeText(ctx, sb.substring(0), Toast.LENGTH_LONG).show();            }
        });
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] arg0) {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arg0.length; ++i) {
            // Returned suggestions are contained in SuggestionsInfo
            final int len = arg0[i].getSuggestionsCount();
            sb.append('\n');

            for (int j = 0; j < len; ++j) {
                sb.append("," + arg0[i].getSuggestionsInfoAt(j));
            }

            sb.append(" (" + len + ")");
        }

        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ctx, sb.substring(0), Toast.LENGTH_LONG).show();            }
        });

    }
}
