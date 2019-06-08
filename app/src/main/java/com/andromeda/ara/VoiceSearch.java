package com.andromeda.ara;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.voice.AlwaysOnHotwordDetector;


import java.util.Locale;

public class VoiceSearch extends Service {
    public VoiceSearch() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    //public abstract void onDetected (AlwaysOnHotwordDetector.EventPayload eventPayload);


}
