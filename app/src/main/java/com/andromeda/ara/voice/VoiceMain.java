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

import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andromeda.ara.R;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.RssFeedModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;

import static com.andromeda.ara.constants.ConstantUtils.AUDIO_FORMAT;
import static com.andromeda.ara.constants.ConstantUtils.AUDIO_FORMAT_PCM;
import static com.andromeda.ara.constants.ConstantUtils.AUDIO_SOURCE;
import static com.andromeda.ara.constants.ConstantUtils.BITS_PER_SAMPLE;
import static com.andromeda.ara.constants.ConstantUtils.BLOCK_ALIGN;
import static com.andromeda.ara.constants.ConstantUtils.BYTE_RATE;
import static com.andromeda.ara.constants.ConstantUtils.CHANNEL_CONFIG;
import static com.andromeda.ara.constants.ConstantUtils.CHUNK_ID;
import static com.andromeda.ara.constants.ConstantUtils.CHUNK_SIZE;
import static com.andromeda.ara.constants.ConstantUtils.FORMAT;
import static com.andromeda.ara.constants.ConstantUtils.NUMBER_OF_CHANNELS;
import static com.andromeda.ara.constants.ConstantUtils.REQUEST_RECORD_AUDIO;
import static com.andromeda.ara.constants.ConstantUtils.SAMPLE_RATE_HZ;
import static com.andromeda.ara.constants.ConstantUtils.SUB_CHUNK_ID_1;
import static com.andromeda.ara.constants.ConstantUtils.SUB_CHUNK_ID_2;
import static com.andromeda.ara.constants.ConstantUtils.SUB_CHUNK__SIZE_1;
import static com.andromeda.ara.util.VoiceMainUtils.writeInt;
import static com.andromeda.ara.util.VoiceMainUtils.writeShort;
import static com.andromeda.ara.util.VoiceMainUtils.writeString;

public class VoiceMain extends AppCompatActivity {
    private FileOutputStream os = null;
    private Thread recordingThread;
    boolean isRecording;
    Integer color;

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
        System.out.println(bufferSizeInBytes);
        setContentView(R.layout.activity_voice_main);
        Toast.makeText(this, "to stop recording hit the 'X' button", Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.listVoice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(Collections.singletonList(new RssFeedModel("hello", "how can I help", "", "", "")));
        recyclerView.setAdapter(adapter);
        requestMicrophonePermission();
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.colorPrimary);
            fab.setBackgroundColor(color);
            while (isRecording){
                fab.setBackgroundColor(color);

            }
        }

        super.onCreate(savedInstanceState);
            startRecording();

    }

    public void back(View view) {
        if (isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopRecording();
            }
        } else onBackPressed();
    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    private void startRecording() {
        audioRecorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(() -> {
            try {
                new File(getCacheDir(), "record.pcm");
                os = new FileOutputStream(getCacheDir() + "/record.pcm");
                while (isRecording) {
                    audioRecorder.read(Data, 0, getRawDataLength(Data));
                    System.out.println(Data[0]);
                    try {
                        os.write(Data, 0, bufferSizeInBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
    }

    private void stopRecording() {
        if (null != audioRecorder) {
            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            recordingThread = null;
            final String[] phrase = new String[1];
            runOnUiThread(() -> {
                try {
                    copyAssets();
                    rawToWave(new File(getCacheDir() + "/record.pcm"), new File(getCacheDir() + "/record.wav"));
                    phrase[0] = new DeepSpeech().run(getCacheDir() + "/record.wav", this.getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<RssFeedModel> rssFeedModels = new ArrayList<>(new Search().main(phrase[0], "0.0", "0.0", getApplicationContext(), VoiceMain.this));
                recyclerView.setAdapter(new Adapter(rssFeedModels));
                try{
                new TTS().start(getApplicationContext(), rssFeedModels.get(0).out);
                }
                catch (Exception ignored){

                }
            });
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
}
