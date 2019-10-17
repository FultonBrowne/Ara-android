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

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VoiceMain extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO = 13;
    private FileOutputStream os = null;
    private Thread recordingThread;
    boolean isRecording;
    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int sampleRateInHz = 16000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

    private byte[] Data = new byte[bufferSizeInBytes];

    private AudioRecord audioRecorder = new AudioRecord(audioSource,
            sampleRateInHz,
            channelConfig,
            audioFormat,
            bufferSizeInBytes);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(bufferSizeInBytes);

        ActivityCompat.requestPermissions(VoiceMain.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        requestMicrophonePermission();
        requestFilePermission();

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startRecording();
        }

    }

    public void back(View view) {
        if(isRecording){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopRecording();
            }
        }
        else onBackPressed();

    }
    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);

        }


    }
    private void requestFilePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 13);
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startRecording() {
        audioRecorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(() -> {


            try {
                new File(getDataDir(),"record.pcm");
                os = new FileOutputStream(getDataDir()+"/record.pcm");
                while (isRecording) {
                    audioRecorder.read(Data, 0, Data.length);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void stopRecording() {
        if (null != audioRecorder) {
            try {
                assert os != null;
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            recordingThread = null;
            try {
                rawToWave(new File(getDataDir()+"/record.pcm"),new File(getDataDir()+"/record.wav"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(new DeepSpeech().run(getDataDir()+ "/record.wav"));
        }
    }
    private void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];

        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(waveFile))) {
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 16000); // sample rate
            writeInt(output, 44100 * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        }
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
    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

}
