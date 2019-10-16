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

import org.mozilla.deepspeech.libdeepspeech.DeepSpeechModel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class DeepSpeech {
    private DeepSpeechModel _m = null;

    synchronized String run(String audioFile) {
        return this.doInference(audioFile);
    }

    private void newModel(String tfliteModel, String alphabet) {
        System.out.println("working");
        if (this._m == null) {
            this._m = new DeepSpeechModel(tfliteModel, alphabet, 50);
        }

    }
    private String doInference(String audioFile) {
        //String actualLabelFilename = audioFile.split("file:///android_asset/", -1)[1];
        String decoded = "err";
        System.out.println("new");

        this.newModel("file:///android_asset/main.tflite", "file:///android_asset/alphabet.txt");
        System.out.println("done");

        try {

            RandomAccessFile wave = new RandomAccessFile(audioFile, "r");

            System.out.println("open file");

            wave.seek(20); char audioFormat = this.readLEChar(wave);
            System.out.println("check is pcm");
            assert (audioFormat == 1); // 1 is PCM
            System.out.println("is pcm");
            // tv_audioFormat.setText("audioFormat=" + (audioFormat == 1 ? "PCM" : "!PCM"));

            wave.seek(22); char numChannels = this.readLEChar(wave);
            assert (numChannels == 1); // MONO
            System.out.println("is mono");
            // tv_numChannels.setText("numChannels=" + (numChannels == 1 ? "MONO" : "!MONO"));

            wave.seek(24); int sampleRate = this.readLEInt(wave);
            assert (sampleRate == 16000); // desired sample rate
            System.out.println("is 1600");
            // tv_sampleRate.setText("sampleRate=" + (sampleRate == 16000 ? "16kHz" : "!16kHz"));

            wave.seek(34); char bitsPerSample = this.readLEChar(wave);
            assert (bitsPerSample == 16); // 16 bits per sample
            // tv_bitsPerSample.setText("bitsPerSample=" + (bitsPerSample == 16 ? "16-bits" : "!16-bits" ));

            wave.seek(40); int bufferSize = this.readLEInt(wave);
            assert (bufferSize > 0);
            // tv_bufferSize.setText("bufferSize=" + bufferSize);

            wave.seek(44);
            System.out.println("wave seek done");
            byte[] bytes = new byte[bufferSize];
            wave.readFully(bytes);
            System.out.println("read wave bytes");

            short[] shorts = new short[bytes.length/2];
            System.out.println("num info");
            // to turn bytes to shorts as either big endian or little endian.
            ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            System.out.println("buffer stuff done");

            long inferenceStartTime = System.currentTimeMillis();
            System.out.println("time");

             decoded = this._m.stt(shorts, shorts.length);
             System.out.println("decoded");


        } catch (IOException e) {
            e.printStackTrace();

        }

        //this._tfliteStatus.setText("Finished! Took " + inferenceExecTime + "ms");

        //this._startInference.setEnabled(true);
        return decoded;
    }
    private char readLEChar(RandomAccessFile f) throws IOException {
        byte b1 = f.readByte();
        byte b2 = f.readByte();
        return (char)((b2 << 8) | b1);
    }
    private int readLEInt(RandomAccessFile f) throws IOException {
        byte b1 = f.readByte();
        byte b2 = f.readByte();
        byte b3 = f.readByte();
        byte b4 = f.readByte();
        return (b1 & 0xFF) | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16 | (b4 & 0xFF) << 24;
    }
}

