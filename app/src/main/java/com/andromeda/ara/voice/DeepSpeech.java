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
import org.mozilla.deepspeech.libdeepspeech.DeepSpeechModel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


class DeepSpeech {
    private DeepSpeechModel _m = null;

    synchronized String run(String audioFile, Context ctx) {
        return this.doInference(audioFile, ctx);
    }

    private void newModel(Context ctx) {
        System.out.println("working");
        if (this._m == null) {
            this._m = new DeepSpeechModel(ctx.getCacheDir() + "/main.tflite", ctx.getCacheDir() + "/alphabet.txt", 50);
        }

    }


    private String doInference(String audioFile, Context ctx) {

        final String[] decoded = {"err"};
        System.out.println("new");


            this.newModel(ctx);

        System.out.println("done");

        try {

            RandomAccessFile wave = new RandomAccessFile(audioFile, "r");

            System.out.println("open file");

            wave.seek(20);
            char audioFormat = this.readLEChar(wave);
            if ((audioFormat != 1)) throw new AssertionError(); // 1 is PCM
            wave.seek(22);
            char numChannels = this.readLEChar(wave);
            if ((numChannels != 1)) throw new AssertionError(); // MONO
            wave.seek(24);
            int sampleRate = this.readLEInt(wave);
            if ((sampleRate != 16000)) throw new AssertionError(); // desired sample rate
            wave.seek(34);
            char bitsPerSample = this.readLEChar(wave);
            if ((bitsPerSample != 16)) throw new AssertionError(); // 16 bits per sample

            wave.seek(40);
            int bufferSize = this.readLEInt(wave);
            if ((bufferSize <= 0)) throw new AssertionError();

            wave.seek(44);
            byte[] bytes = new byte[bufferSize];
            wave.readFully(bytes);
            short[] shorts = new short[bytes.length / 2];
            System.out.println("num info");
            ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

                    decoded[0] = this._m.stt(shorts, shorts.length);
                System.out.println(decoded[0]);




            System.out.println(decoded[0]);


        } catch (IOException e) {
            e.printStackTrace();

            return decoded[0];
        }
        return decoded[0];
    }

    private char readLEChar(RandomAccessFile f) throws IOException {
        byte b1 = f.readByte();
        byte b2 = f.readByte();
        return (char) ((b2 << 8) | b1);
    }

    private int readLEInt(RandomAccessFile f) throws IOException {
        byte b1 = f.readByte();
        byte b2 = f.readByte();
        byte b3 = f.readByte();
        byte b4 = f.readByte();
        return (b1 & 0xFF) | (b2 & 0xFF) << 8 | (b3 & 0xFF) << 16 | (b4 & 0xFF) << 24;
    }
}

