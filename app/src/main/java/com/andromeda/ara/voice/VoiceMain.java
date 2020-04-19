
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
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andromeda.ara.R;
import com.andromeda.ara.client.models.FeedModel;
import com.andromeda.ara.client.models.SkillsModel;
import com.andromeda.ara.client.search.Actions;
import com.andromeda.ara.models.OutputModel;
import com.andromeda.ara.models.TabModel;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.skills.Parse;
import com.andromeda.ara.skills.RunActions;
import com.andromeda.ara.skills.SearchFunctions;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.ApiOutputToRssFeed;
import com.andromeda.ara.util.DownloadTask;
import com.andromeda.ara.util.GetUrlAra;
import com.andromeda.ara.util.JsonParse;
import com.andromeda.ara.util.SetFeedData;
import com.andromeda.ara.util.SpellChecker;
import com.andromeda.ara.util.TabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.andromeda.ara.constants.ConstantUtils.AUDIO_FORMAT;
import static com.andromeda.ara.constants.ConstantUtils.AUDIO_SOURCE;
import static com.andromeda.ara.constants.ConstantUtils.CHANNEL_CONFIG;
import static com.andromeda.ara.constants.ConstantUtils.REQUEST_RECORD_AUDIO;
import static com.andromeda.ara.constants.ConstantUtils.SAMPLE_RATE_HZ;

public class VoiceMain extends AppCompatActivity implements SearchFunctions, Actions, SetFeedData {
    private FileOutputStream os = null;
    private Thread recordingThread;
    boolean isRecording;
    DeepSpeech deepSpeech;
    Boolean blankRunning = false;
    public Context ctx = this;

    private int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);

    private byte[] Data = new byte[bufferSizeInBytes];
     ByteArrayOutputStream byteIS = new ByteArrayOutputStream();
    RecyclerView recyclerView;

    private AudioRecord audioRecorder = new AudioRecord(AUDIO_SOURCE,
            SAMPLE_RATE_HZ,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSizeInBytes);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File file = new File(ctx.getCacheDir() + "/main.tflite");
        File file1 = new File(this.getCacheDir() + "/alphabet.txt");
        if(file1.exists() && file.exists()){
        System.out.println(bufferSizeInBytes);
        setContentView(R.layout.activity_voice_main);
        deepSpeech = new DeepSpeech(this);
        recyclerView = findViewById(R.id.listVoice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(new ArrayList<FeedModel>(), this);
        recyclerView.setAdapter(adapter);
        requestMicrophonePermission();



        startRecording(null);
        }
        else{
            ProgressDialog mProgressDialog;
            mProgressDialog = new ProgressDialog(ctx);
            mProgressDialog.setMessage(ctx.getString(R.string.download));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            DownloadTask downloadTask = new DownloadTask(this, ctx.getCacheDir() + "/main.tflite", mProgressDialog);
            DownloadTask downloadTask2 = new DownloadTask(this, ctx.getCacheDir() + "/alphabet.txt", mProgressDialog);

            downloadTask.execute("https://arafilestore.file.core.windows.net/ara-server-files/main.tflite?sv=2019-02-02&ss=bfqt&srt=sco&sp=rwdlacup&se=2024-04-01T22:11:11Z&st=2019-12-19T15:11:11Z&spr=https&sig=lfjMHSahA6fw8enCbx0hFTE1uAVJWvPmC4m6blVSuuo%3D");
            downloadTask2.execute("https://arafilestore.file.core.windows.net/ara-server-files/alphabet.txt?sv=2019-02-02&ss=bfqt&srt=sco&sp=rwdlacup&se=2024-04-01T22:11:11Z&st=2019-12-19T15:11:11Z&spr=https&sig=lfjMHSahA6fw8enCbx0hFTE1uAVJWvPmC4m6blVSuuo%3D");
            mProgressDialog.setOnCancelListener(dialog -> {
                downloadTask.cancel(true); //cancel the task
            });
            mProgressDialog.show();

        }

    }




    public void back(View view) {
        if (isRecording) {
                stopRecording(null);
        }
        onBackPressed();
    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }
    private synchronized void startRecording(@Nullable String link) {
        recordingThread = new Thread(() -> {
        audioRecorder.startRecording();

            try {
                new File(getCacheDir(), "record.pcm");
                os = new FileOutputStream(getCacheDir() + "/record.pcm");
                isRecording = true;
                VoiceView voiceView = findViewById(R.id.floatingActionButton);
                deepSpeech.updateV3(this);
                while (isRecording) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Float radius = (Float) (float) Math.log10(Math.max(1, Data[0] / 10f)) * VoiceView.dp2px(getApplicationContext(), 20);
                            System.out.println(radius);
                            voiceView.animateRadius(Math.abs(((Byte)Data[0])));
                        }
                    });

                    audioRecorder.read(Data, 0, getRawDataLength(Data));
                    //voiceView.animateRadius(radius);
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
                        byteIS.write(Data);
                        os.write(Data, 0, bufferSizeInBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        final MediaPlayer mp = new MediaPlayer();
        mp.reset();
        AssetFileDescriptor afd;
        try{
            afd = getAssets().openFd("start.mp3");
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(mp1 -> {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "go", Toast.LENGTH_LONG).show());
                recordingThread.start();

            });
        }
        catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }
    private String recordForString() throws InterruptedException {
        String[] phrase = new String[1];
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "go", Toast.LENGTH_LONG).show();

                    }
                });
            }
            catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }

            audioRecorder.startRecording();

            try {
                new File(getCacheDir(), "record.pcm");
                os = new FileOutputStream(getCacheDir() + "/record.pcm");
                VoiceView voiceView = findViewById(R.id.floatingActionButton);

                isRecording = true;
                deepSpeech.updateV3(this);
                while (isRecording) {
                    audioRecorder.read(Data, 0, getRawDataLength(Data));
                    System.out.println(Data[0]);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Float radius = (Float) (float) Math.log10(Math.max(1, Data[0] / 10f)) * VoiceView.dp2px(getApplicationContext(), 20);
                            System.out.println(radius);
                            voiceView.animateRadius(Math.abs(((Byte)Data[0])));
                        }
                    });                   if (Data[0] == 0) {
                        System.out.println("blank");
                        blankRunning = false;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (!blankRunning) {

                                        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
                                        fab2.setVisibility(View.VISIBLE);
                                    }
                                });

                            }
                        }, 4000);
                    } else blankRunning = true;

                    try {
                        byteIS.write(Data);
                        os.write(Data, 0, bufferSizeInBytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    phrase[0] = deepSpeech.voiceV3();
                    phrase[0] = new SpellChecker().check(phrase[0]);
                    deepSpeech = new DeepSpeech(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
        recordingThread.join();
        return phrase[0];

    }

    private void stopRecording(@Nullable String link) {
        isRecording = false;
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


            Thread recognize = new Thread(() -> {
                System.out.println("ok");
                try {
                    //phrase[0] = new DeepSpeech().voiceV2(byteIS.toByteArray(), this);
                    phrase[0] = deepSpeech.voiceV3();
                    phrase[0] = new SpellChecker().check(phrase[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (link == null) runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                         new Search().main(phrase[0], VoiceMain.this, VoiceMain.this, new TTS(), new ArrayList<>(), VoiceMain.this, VoiceMain.this::setData);

                    }
                });
                else new Search().outputPing(link.replace("TERM", phrase[0]), getApplicationContext(), this, this);
                deepSpeech = new DeepSpeech(this);


            });
            recognize.setPriority(Thread.MAX_PRIORITY);
            recognize.start();

    }

    private int getRawDataLength(byte[] rawData) {
        return rawData.length;
    }


    public void exit(View view) {
        onBackPressed();
    }

    public void record(View view) {
        if (isRecording) {
            stopRecording(null);

        }
        else {
            audioRecorder = new AudioRecord(AUDIO_SOURCE,
                    SAMPLE_RATE_HZ,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    bufferSizeInBytes);
            startRecording(null);
        }
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
        startRecording(link);

    }

    @NonNull
    @Override
    public String callForString(@NotNull String m) {
        new TTS().start(getApplicationContext(), m);
        try {
            return recordForString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public void onTabTrigger(@NotNull TabModel data) {
        try {
            ArrayList<OutputModel> outputModels = new JsonParse().search(new GetUrlAra().getIt(new URL(data.getUrl())));
            ArrayList<FeedModel> feedModels = new ApiOutputToRssFeed().main(outputModels);
            recyclerView.setAdapter(new Adapter(feedModels, this));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addTabData(@NotNull List<TabModel> data) {
        RecyclerView tabs = findViewById(R.id.tabs2);
        tabs.setVisibility(View.VISIBLE);
        tabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        tabs.setAdapter(new TabAdapter(data, this));

    }
    @Override
    public <T> T parseYaml(@NotNull String s) {
        return (T) new Parse().yamlArrayToObject(s, SkillsModel.class);
    }

    @Override
    public void runActions(@NotNull ArrayList<SkillsModel> arrayList, String term) {
        new RunActions().doIt(arrayList, term, this, this, this);
    }

    @Override
    public void setData(@NotNull ArrayList<FeedModel> feedModel) {
        runOnUiThread(() -> {
            recyclerView.setAdapter(new Adapter(feedModel, this));

        });
    }
}
