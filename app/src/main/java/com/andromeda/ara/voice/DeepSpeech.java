

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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


class DeepSpeech {


    private DeepSpeechModel _m = null;
    public String decode = "";
    private VoiceMain voiceMain;
    Thread thread;
    DeepSpeechStreamingState stream;
    public DeepSpeech(Context ctx){
        newModel(ctx);
    }
    private int count = 0;
    public ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();




    private void newModel(Context ctx) {
        System.out.println("working");
        if (this._m == null) {
            this._m = new DeepSpeechModel(ctx.getCacheDir().getAbsolutePath() + "/main2.tflite");
        }
        stream = _m.createStream();

    }

    void updateV3(VoiceMain voiceMain){
       this.voiceMain = voiceMain;
       thread = new Thread(()->{
           try {
               Thread.sleep(200);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           while (voiceMain.isRecording){

           byte[] bytes = voiceMain.byteIS.toByteArray();
            voiceMain.byteIS.reset();
            short[] shorts = new short[bytes.length / 2];
            ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            _m.feedAudioContent(stream, shorts, shorts.length);

        }});
       thread.start();

    }
    public String voiceV3(){	
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	VoiceMain voiceMain = this.voiceMain;
	byte[] bytes = voiceMain.byteIS.toByteArray();
	voiceMain.byteIS.reset();
	short[] shorts = new short[bytes.length / 2];
	ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
	_m.feedAudioContent(stream, shorts, shorts.length);	
        return _m.finishStream(stream);
    }

}

