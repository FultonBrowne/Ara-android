package com.andromeda.ara;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.andromeda.ara.util.calUtility;
import com.andromeda.ara.util.locl;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import org.tensorflow.lite.Interpreter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class MainActivity extends AppCompatActivity implements popupuiListDialogFragment.Listener {


    private static final String LOG_TAG ="e" ;
    public SwipeRefreshLayout mSwipeLayout;
    short[] recordingBuffer = new short[RECORDING_LENGTH];
    int recordingOffset = 0;
    boolean shouldContinue = true;
    private Thread recordingThread;
    boolean shouldContinueRecognition = true;
    private Thread recognitionThread;
    private final ReentrantLock recordingBufferLock = new ReentrantLock();
    private RecognizeCommands recognizeCommands = null;
    private TensorFlowInferenceInterface inferenceInterface;
    private List<String> labels = new ArrayList<String>();
    private List<String> displayedLabels = new ArrayList<>();
    //private voiceInput recognizeCommands = null;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_DURATION_MS = 1000;
    private static final int RECORDING_LENGTH = (int) (SAMPLE_RATE * SAMPLE_DURATION_MS / 1000);
    private static final long AVERAGE_WINDOW_DURATION_MS = 500;
    private static final float DETECTION_THRESHOLD = 0.70f;
    private static final int SUPPRESSION_MS = 1500;
    private Interpreter tfLite;
    String resulttxt = "err";
    private static final int MINIMUM_COUNT = 3;
    private static final long MINIMUM_TIME_BETWEEN_SAMPLES_MS = 30;
    private static final String LABEL_FILENAME = "file:///android_asset/conv_actions_labels.txt";
    private static final String MODEL_FILENAME = "file:///android_asset/conv_actions_frozen.tflite";
    private static final String INPUT_DATA_NAME = "decoded_sample_data:0";
    private static final String SAMPLE_RATE_NAME = "decoded_sample_data:1";
    private static final String OUTPUT_SCORES_NAME = "labels_softmax";

    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
            throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    Boolean done = false;


    // UI elements.
    private static final int REQUEST_RECORD_AUDIO = 13;

    int searchmode = 1;
    double lat;
    double log;
    String mTime = "hello";
    Toolbar mActionBarToolbar;
    private FusedLocationProviderClient fusedLocationClient;
    private Drawer result1 = null;
    String title1;

    String web1;
    //RssFeedModel test222 = new search().main("hi",1);
    public int mode = 1;


    private RecyclerView.Adapter mAdapter;
    List<RssFeedModel> rssFeedModel1 = new ArrayList<>();

    public static List<RssFeedModel> parseFeed() throws IOException {
        String mFeedTitle;
        String mFeedImage;
        String mFeedLink;
        String mFeedDescription;


        List<SyndEntry> mTest;
        List<RssFeedModel> items = new ArrayList<>();
        XmlReader xmlReader = null;
        try {
            URL feed = new URL("https://www.espn.com/espn/rss/news/rss.xml");
            //URL feed = new URL("http://localhost:8000/test");


            feed.openConnection();
            xmlReader = new XmlReader(feed);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feedAllData = new SyndFeedInput().build(xmlReader);
            for (Iterator iterator = feedAllData.getEntries().iterator(); iterator
                    .hasNext(); ) {
                SyndEntry syndEntry = (SyndEntry) iterator.next();
                mFeedDescription = syndEntry.getDescription().getValue();
                mFeedTitle = syndEntry.getTitle();
                mFeedLink = syndEntry.getLink();

                RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle, "");
                items.add(rssFeedModel);


            }


        } catch (IOException e) {
            mFeedLink = "err";
            mFeedTitle = "err";
            mFeedDescription = "err";
            RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle, "");
            items.add(rssFeedModel);

        } catch (FeedException e) {
            mFeedLink = "err";
            mFeedTitle = "err";
            mFeedDescription = "err";
            RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle, "");
            items.add(rssFeedModel);

        } finally {
            if (xmlReader != null)
                xmlReader.close();
        }


        return items;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final tagManager main53 = new tagManager(this);
        final Context ctx = this;
        requestLocationPermission();



        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time();
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CALENDAR},
                1);
        ;


        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        RecyclerView recyclerView = findViewById(R.id.list);

        String test1 = PreferenceManager.getDefaultSharedPreferences(this).getString("example_text", "defaultStringIfNothingFound");


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("home");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("tags");
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("food");
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("shopping");
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("agenda");
        // lose dis if crash
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                //.withHeaderBackground(R.drawable.back)


                .addProfiles(
                        new ProfileDrawerItem().withName("name").withEmail("email@gmail.com").withIcon(getResources().getDrawable(R.drawable.example_appwidget_preview))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


//Now create your drawer and pass the AccountHeader.Result


//create the drawer and remember the `Drawer` result object
        result1 = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mActionBarToolbar)
                .withAccountHeader(headerResult)

                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            mode = 1;
                            Toast.makeText(getApplicationContext(), "number 1", Toast.LENGTH_SHORT).show();
                            try {
                                RecyclerView recyclerView = findViewById(R.id.list);

                                rssFeedModel1 = (parseFeed());
                                mAdapter = new Adapter(rssFeedModel1);

                                recyclerView.setAdapter(mAdapter);


                                //recyclerView.setAdapter(new Adapter(parseFeed()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (drawerItem.getIdentifier() == 2) {
                            Toast.makeText(getApplicationContext(), "number 2", Toast.LENGTH_SHORT).show();

                            RecyclerView recyclerView = findViewById(R.id.list);
                            main53.open();
                            //final Cursor cursor = main53.fetch();
                            Cursor cursor = main53.fetch();
                            RssFeedModel test = new RssFeedModel("", "", "", "");


                            if (cursor != null && cursor.moveToFirst()) {
                                cursor.moveToFirst();

                                while (!cursor.isAfterLast()) {
                                    rssFeedModel1.clear();
                                    title1 = cursor.getString(1);
                                    web1 = cursor.getString(2);
                                    test = new RssFeedModel(title1, web1, "", "");
                                    rssFeedModel1.add(test);
                                    cursor.moveToNext();
                                }
                            } else {
                                title1 = "nothing";
                                web1 = "reload app";
                                test = new RssFeedModel(title1, web1, "", "");
                                rssFeedModel1.add(test);


                            }
                            //RssFeedModel test = new RssFeedModel( title1, web1, "","");
                            //rssFeedModel1.clear();
                            //rssFeedModel1.add(test);
                            mAdapter = new Adapter(rssFeedModel1);


                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));

                        } else if (drawerItem.getIdentifier() == 3) {
                            mode = 2;
                            Toast.makeText(getApplicationContext(), "number 3", Toast.LENGTH_SHORT).show();
                            RecyclerView recyclerView = findViewById(R.id.list);


                            RssFeedModel test = new RssFeedModel("food", "zomato.com", "food near you coming soon", "");
                            rssFeedModel1.clear();
                            rssFeedModel1.add(test);


                            //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");


                            new locl(ctx);
                            ArrayList<RssFeedModel> main352 = new food().getFood(Double.toString(locl.longitude), Double.toString(locl.latitude));
                            rssFeedModel1 = main352;
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } else if (drawerItem.getIdentifier() == 4) {
                            mode = 3;
                            Toast.makeText(getApplicationContext(), "number 4", Toast.LENGTH_SHORT).show();
                            RecyclerView recyclerView = findViewById(R.id.list);


                            RssFeedModel test = new RssFeedModel("food", "zomato.com", "food near you coming soon", "");
                            rssFeedModel1.clear();


                            //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");


                            new locl(ctx);
                            ArrayList<RssFeedModel> main352 = new shopping().getShops(Double.toString((locl.longitude)), Double.toString((locl.latitude)));
                            rssFeedModel1 = main352;
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } else if (drawerItem.getIdentifier() == 5) {
                            mode = 3;
                            Toast.makeText(getApplicationContext(), "number 5", Toast.LENGTH_SHORT).show();
                            RecyclerView recyclerView = findViewById(R.id.list);


                            RssFeedModel test = new RssFeedModel("food", "zomato.com", "food near you coming soon", "");
                            rssFeedModel1.clear();
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_CALENDAR},
                                    1);


                            //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");

                            ArrayList<RssFeedModel> main352 = (ArrayList<RssFeedModel>) calUtility.readCalendarEvent(ctx);
                            rssFeedModel1 = main352;
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        }


                        return false;
                        // do something with the clicked item :D
                    }


                })
                .build();

        getSupportActionBar().setTitle(mTime);
        StrictMode.setThreadPolicy(policy);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent browserIntent;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssFeedModel1.get(position).link));

                startActivity(browserIntent);
            }


            @Override
            public void onLongClick(View view, int position) {
                insert(rssFeedModel1.get(position).title, rssFeedModel1.get(position).link, main53);

                Toast.makeText(getApplicationContext(), "tagged", Toast.LENGTH_SHORT).show();

            }
        }));
        mSwipeLayout = findViewById(R.id.swipe1);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    rssFeedModel1.clear();
                    List<RssFeedModel> items2 = (parseFeed());
                    rssFeedModel1 = (parseFeed());
                    rssFeedModel1.addAll(items2);
                    mAdapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            rssFeedModel1 = (parseFeed());
            mAdapter = new Adapter(rssFeedModel1);

            recyclerView.setAdapter(mAdapter);


            //recyclerView.setAdapter(new Adapter(parseFeed()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Start the recording and recognition thread
                Activity activity = (Activity) ctx;

                String phrase = new com.andromeda.ara.voice.run().run(ctx, activity);
                Toast.makeText(ctx, phrase, Toast.LENGTH_LONG);


                //List<RssFeedModel> phrase2 = new search().main(phrase, 1);

                //rssFeedModel1.addAll(0, phrase2);
                //mAdapter.notifyDataSetChanged();


            }
        });

    }

    public void yourMethodName(MenuItem menuItem) {
        startActivity(new Intent(this, com.andromeda.ara.SettingsActivity.class));
    }


    public void about(MenuItem menuItem) {
        startActivity(new Intent(this, com.andromeda.ara.about.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                String input = query;
                //RssFeedModel rssFeedModel2 = (new com.andromeda.ara.Wolfram().Wolfram1(input));
                ArrayList<RssFeedModel> rssFeedModel2 = (new search().main(query, mode));
                rssFeedModel1.addAll(0, rssFeedModel2);
                mAdapter.notifyDataSetChanged();


                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result1 != null && result1.isDrawerOpen()) {
            result1.closeDrawer();
        } else if (result1 != null && !result1.isDrawerOpen()) {
            result1.openDrawer();
        } else {
            super.onBackPressed();
        }
    }

    void insert(String main, String link, tagManager main53) {

        main53.open();
        main53.insert(main, link);
        main53.close();
    }

    @Override
    public void onpopupuiClicked(int position) {
        Intent browserIntent;
        try {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parseFeed().get(position).link));
            startActivity(browserIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(26)
    public String time() {

        int mHour = LocalTime.now().getHour();
        if (mHour < 12) {
            mTime = "Good morning";
        } else if (mHour >= 12 && mHour < 16) {
            mTime = "good afternoon";

        } else {
            mTime = "Good evening";
        }
        return mTime;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }


    }
    public synchronized void startRecognition() {
        if (recognitionThread != null) {
            return;
        }
        shouldContinueRecognition = true;
        recognitionThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                recognize();
                            }
                        });
        recognitionThread.start();
    }

    public synchronized void stopRecognition() {
        if (recognitionThread == null) {
            return;
        }
        shouldContinueRecognition = false;
        recognitionThread = null;
    }

    public synchronized void startRecording() {
        if (recordingThread != null) {
            return;
        }
        shouldContinue = true;
        recordingThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                record();
                            }
                        });
        recordingThread.start();
    }

    private void record() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        // Estimate the buffer size we'll need for this device.
        int bufferSize =
                AudioRecord.getMinBufferSize(
                        SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2;
        }
        short[] audioBuffer = new short[bufferSize / 2];

        AudioRecord record =
                new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!");
            return;
        }

        record.startRecording();

        Log.v(LOG_TAG, "Start recording");

        // Loop, gathering audio data and copying it to a round-robin buffer.
        while (shouldContinue) {
            int numberRead = record.read(audioBuffer, 0, audioBuffer.length);
            int maxLength = recordingBuffer.length;
            int newRecordingOffset = recordingOffset + numberRead;
            int secondCopyLength = Math.max(0, newRecordingOffset - maxLength);
            int firstCopyLength = numberRead - secondCopyLength;
            // We store off all the data for the recognition thread to access. The ML
            // thread will copy out of this buffer into its own, while holding the
            // lock, so this should be thread safe.
            recordingBufferLock.lock();
            try {
                System.arraycopy(audioBuffer, 0, recordingBuffer, recordingOffset, firstCopyLength);
                System.arraycopy(audioBuffer, firstCopyLength, recordingBuffer, 0, secondCopyLength);
                recordingOffset = newRecordingOffset % maxLength;
            } finally {
                recordingBufferLock.unlock();
            }
            stopRecognition();
        }

        record.stop();
        record.release();
    }

    public synchronized void stopRecording() {
        if (recordingThread == null) {
            return;
        }
        shouldContinue = false;
        recordingThread = null;
    }

    private void recognize() {

        Log.v(LOG_TAG, "Start recognition");

        short[] inputBuffer = new short[RECORDING_LENGTH];
        float[][] floatInputBuffer = new float[RECORDING_LENGTH][1];
        float[][] outputScores = new float[1][labels.size()];
        int[] sampleRateList = new int[] {SAMPLE_RATE};

        // Loop, grabbing recorded data and running the recognition model on it.
        while (shouldContinueRecognition) {
            long startTime = new Date().getTime();
            // The recording thread places data in this round-robin buffer, so lock to
            // make sure there's no writing happening and then copy it to our own
            // local version.
            recordingBufferLock.lock();
            try {
                int maxLength = recordingBuffer.length;
                int firstCopyLength = maxLength - recordingOffset;
                int secondCopyLength = recordingOffset;
                System.arraycopy(recordingBuffer, recordingOffset, inputBuffer, 0, firstCopyLength);
                System.arraycopy(recordingBuffer, 0, inputBuffer, firstCopyLength, secondCopyLength);
            } finally {
                recordingBufferLock.unlock();
            }

            // We need to feed in float values between -1.0f and 1.0f, so divide the
            // signed 16-bit inputs.
            for (int i = 0; i < RECORDING_LENGTH; ++i) {
                floatInputBuffer[i][0] = inputBuffer[i] / 32767.0f;
            }

            Object[] inputArray = {floatInputBuffer, sampleRateList};
            Map<Integer, Object> outputMap = new HashMap<>();
            outputMap.put(0, outputScores);

            // Run the model.
            tfLite.runForMultipleInputsOutputs(inputArray, outputMap);

            // Use the smoother to figure out if we've had a real recognition event.
            long currentTime = System.currentTimeMillis();
            final RecognizeCommands.RecognitionResult result =
                    recognizeCommands.processLatestResults(outputScores[0], currentTime);
            // lastProcessingTimeMs = new Date().getTime() - startTime;
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {

                            //inferenceTimeTextView.setText(lastProcessingTimeMs + " ms");

                            // If we do have a new command, highlight the right list entry.
                            if (!result.foundCommand.startsWith("_") && result.isNewCommand) {
                                int labelIndex = -1;
                                for (int i = 0; i < labels.size(); ++i) {
                                    if (labels.get(i).equals(result.foundCommand)) {
                                        labelIndex = i;
                                    }
                                }
                                switch (labelIndex - 2) {
                                    case 0:
                                        resulttxt = "yes";

                                        break;
                                    case 1:
                                        resulttxt = "no";
                                        break;
                                    case 2:
                                        resulttxt = "up";
                                        break;
                                    case 3:
                                        resulttxt = "down" ;
                                        break;
                                    case 4:
                                        resulttxt = "left";
                                        break;
                                    case 5:
                                        resulttxt = "right";
                                        break;
                                    case 6:
                                        resulttxt = "on";
                                        break;
                                    case 7:
                                        resulttxt = "off";
                                        break;
                                    case 8:
                                        resulttxt = "stop";
                                        break;
                                    case 9:
                                        resulttxt = "go";
                                        break;
                                }


                            }

                        }
                    });
            try {
                // We don't need to run too frequently, so snooze for a bit.
                Thread.sleep(MINIMUM_TIME_BETWEEN_SAMPLES_MS);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        Log.v(LOG_TAG, "End recognition");
        if (resulttxt == null){
            resulttxt = "err";

        }
    }


}










