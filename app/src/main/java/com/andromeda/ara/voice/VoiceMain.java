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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.andromeda.ara.R;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.skills.SearchFunctions;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.DownloadTask;
import com.andromeda.ara.util.RssFeedModel;
import com.andromeda.ara.util.SpellChecker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

import static com.andromeda.ara.constants.ConstantUtils.*;

public class VoiceMain extends AppCompatActivity implements SearchFunctions {
    private FileOutputStream os = null;
    private Thread recordingThread;
    boolean isRecording;
    ImageView imageView;
    Boolean blankRunning = false;
    public Context ctx = this;

    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);

    private byte[] Data = new byte[bufferSizeInBytes];
    private ByteArrayOutputStream byteIS = new ByteArrayOutputStream();
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
        recyclerView = findViewById(R.id.listVoice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(Collections.singletonList(new RssFeedModel("hello", "how can I help", "", "", "", true)), this);
        recyclerView.setAdapter(adapter);
        requestMicrophonePermission();


        super.onCreate(savedInstanceState);

        startRecording(null);
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
                stopRecording(null);
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
    private synchronized void startRecording(@Nullable String link) {
        recordingThread = new Thread(() -> {
        final MediaPlayer mp = new MediaPlayer();
        mp.reset();
        AssetFileDescriptor afd;
        try{
            afd = getAssets().openFd("start.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
            while(mp.isPlaying()) System.out.println("playing");
        }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        audioRecorder.startRecording();
        isRecording = true;

            try {
                new File(getCacheDir(), "record.pcm");
                os = new FileOutputStream(getCacheDir() + "/record.pcm");

                while (isRecording) {
                    audioRecorder.read(Data, 0, getRawDataLength(Data));
                    System.out.println(Data[0]);
                    byteIS.write(Data);
                    if (Data[0] == 0) {
                        System.out.println("blank");
                        blankRunning = false;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (!blankRunning) {
                                        stopRecording(link);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
    }

    private void stopRecording(@Nullable String link) {
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
            recognize(link);
        }
    }

    private void recognize(@Nullable String link) {
        final String[] phrase = new String[1];
        File file = new File(ctx.getCacheDir() + "/main.tflite");
        if(!file.exists()){
            ProgressDialog mProgressDialog;
            mProgressDialog = new ProgressDialog(ctx);
            mProgressDialog.setMessage(ctx.getString(R.string.download));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            final DownloadTask downloadTask = new DownloadTask(this, ctx.getCacheDir() + "/main.tflite", mProgressDialog);
            downloadTask.execute("https://arafilestore.file.core.windows.net/ara-server-files/main.tflite?sv=2019-02-02&ss=bfqt&srt=sco&sp=rwdlacup&se=2024-04-01T22:11:11Z&st=2019-12-19T15:11:11Z&spr=https&sig=lfjMHSahA6fw8enCbx0hFTE1uAVJWvPmC4m6blVSuuo%3D");

            mProgressDialog.setOnCancelListener(dialog -> {
                downloadTask.cancel(true); //cancel the task
            });
            mProgressDialog.show();
        }
        File file1 = new File(this.getCacheDir() + "/alphabet.txt");
        if(!file1.exists()){
            ProgressDialog mProgressDialog;
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(ctx.getString(R.string.download));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            final DownloadTask downloadTask = new DownloadTask(this, ctx.getCacheDir() + "/alphabet.txt", mProgressDialog);
            downloadTask.execute("https://arafilestore.file.core.windows.net/ara-server-files/alphabet.txt?sv=2019-02-02&ss=bfqt&srt=sco&sp=rwdlacup&se=2024-04-01T22:11:11Z&st=2019-12-19T15:11:11Z&spr=https&sig=lfjMHSahA6fw8enCbx0hFTE1uAVJWvPmC4m6blVSuuo%3D");

            mProgressDialog.setOnCancelListener(dialog -> {
                downloadTask.cancel(true); //cancel the task
            });
        }
        else {
            Thread recognize = new Thread(() -> {
                try {
                    //phrase[0] = new DeepSpeech().voiceV2(byteIS.toByteArray(), this);
                    phrase[0] = new DeepSpeech().voiceV3(byteIS, this);
                    phrase[0] = new SpellChecker().check(phrase[0]);
                    byteIS.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList<RssFeedModel> rssFeedModels = new ArrayList<>();
                new Search().main(phrase[0], getApplicationContext(), VoiceMain.this, this, recyclerView,new TTS());

            });
            recognize.setPriority(Thread.MAX_PRIORITY);
            recognize.start();
        }
    }

    private int getRawDataLength(byte[] rawData) {
        return rawData.length;
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
        startRecording(null);
    }


    @Override
    public void callBack(@NotNull String m, @NotNull String link) {
        new TTS().start(this, m);
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
        audioRecorder = new AudioRecord(AUDIO_SOURCE,
                SAMPLE_RATE_HZ,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSizeInBytes);
        runTransition();
        startRecording(link);

    }

    @NotNull
    @Override
    public String callForString(@NotNull String m) {
        return null;
    }
}
