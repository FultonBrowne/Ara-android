package com.andromeda.ara;

import android.content.Intent;
import android.speech.RecognitionService;
import android.util.Log;
/**
 * Stub recognition service needed to be a complete voice interactor.
 */
public class MainAraRS extends RecognitionService {
    private static final String TAG = "MainRecognitionService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }
    @Override
    protected void onStartListening(Intent recognizerIntent, Callback listener) {
        Log.d(TAG, "onStartListening");
    }
    @Override
    protected void onCancel(Callback listener) {
        Log.d(TAG, "onCancel");
    }
    @Override
    protected void onStopListening(Callback listener) {
        Log.d(TAG, "onStopListening");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
