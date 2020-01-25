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
import org.mozilla.deepspeech.libdeepspeech.DeepSpeechStreamingState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


class DeepSpeech {
    private DeepSpeechModel _m = null;
    public String decode = "";
    DeepSpeechStreamingState stream;
    public DeepSpeech(Context ctx){
        newModel(ctx);
    }
    private int count = 0;
    public ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();




    private void newModel(Context ctx) {
        System.out.println("working");
        if (this._m == null) {
            this._m = new DeepSpeechModel(ctx.getCacheDir() + "/main.tflite", ctx.getCacheDir() + "/alphabet.txt", 50);
        }
        stream = _m.createStream();

    }
    public String voiceV2(byte[] bytes, Context ctx){
        newModel(ctx);
        short[] shorts = new short[bytes.length / 2];
        System.out.println("num info");
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        System.out.println("done");
        return this._m.stt(shorts, shorts.length);
    }
    void updateV3(byte[] bytes){
        try {
            byteArrayOutputStream.write(bytes);
            count = count + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(count == 16) {
            count = 0;
            System.out.println("thread");
            short[] shorts = new short[byteArrayOutputStream.toByteArray().length / 2];
            System.out.println("num info");
            ByteBuffer.wrap(byteArrayOutputStream.toByteArray()).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                _m.feedAudioContent(stream, shorts, shorts.length);


        }


    }
    public String voiceV3(){
        return _m.finishStream(stream);
    }

}

