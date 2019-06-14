package com.andromeda.ara;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.service.voice.VoiceInteractionSession;
import android.view.View;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class VoiceUi extends VoiceInteractionSession

        //implements View.OnClickListener
        {
            View mContentView;


    @Override
    public void onCreate() {
        super.onCreate();
        ActivityManager am = getContext().getSystemService(ActivityManager.class);
        am.setWatchHeapLimit(40 * 1024 * 1024);
    }

    public VoiceUi(Context context) {
                super(context);
            }


            @Override
            public View onCreateContentView(){
                mContentView = getLayoutInflater().inflate(R.layout.voiceuitest, null);
                return mContentView;


            }
        }
