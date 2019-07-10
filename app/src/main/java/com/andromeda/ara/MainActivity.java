package com.andromeda.ara;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.time.*;


public class MainActivity extends AppCompatActivity implements popupuiListDialogFragment.Listener {


    public SwipeRefreshLayout mSwipeLayout;
    String mTime = "hello";
    Toolbar mActionBarToolbar;

    public static List<RssFeedModel> parseFeed() throws IOException {
        String mFeedTitle;
        String mFeedLink;
        String mFeedDescription;

        List<SyndEntry> mTest;
        List<RssFeedModel> items = new ArrayList<>();
        XmlReader xmlReader = null;
        try {
            URL feed = new URL("https://www.espn.com/espn/rss/news/rss.xml");


            feed.openConnection();
            xmlReader = new XmlReader(feed);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feedAllData = new SyndFeedInput().build(xmlReader);
            mTest = feedAllData.getEntries();
            mFeedDescription = feedAllData.getDescription();
            mFeedTitle = feedAllData.getTitle();
            mFeedLink = feedAllData.getLink();
            for (Iterator iterator = feedAllData.getEntries().iterator(); iterator
                    .hasNext(); ) {
                SyndEntry syndEntry = (SyndEntry) iterator.next();
                mFeedDescription = syndEntry.getDescription().getValue();
                mFeedTitle = syndEntry.getTitle();
                mFeedLink = syndEntry.getLink();
                RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle);
                items.add(rssFeedModel);


            }


        } catch (IOException e) {
            mFeedLink = "err";
            mFeedTitle = "err";
            mFeedDescription = "err";
            RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle);
            items.add(rssFeedModel);

        } catch (FeedException e) {
            mFeedLink = "err";
            mFeedTitle = "err";
            mFeedDescription = "err";
            RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle);
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
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time();
        }
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle("My title");
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(mTime);
        StrictMode.setThreadPolicy(policy);
        RecyclerView recyclerView = findViewById(R.id.list);
        mSwipeLayout = findViewById(R.id.swipe1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {


            recyclerView.setAdapter(new Adapter(parseFeed()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupuiListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
            }
        });

    }

    public void yourMethodName(MenuItem menuItem) {
        startActivity(new Intent(this, com.andromeda.ara.SettingsActivity.class));
    }

    /**
     * public List<RssFeedModel> parseFeed() {
     * <p>
     * <p>
     * String[] item1 = {
     * "infotest1"
     * };
     * String[] item2 = {
     * "infotest1"
     * };
     * String[] item3 = {
     * "infotest1"
     * };
     * <p>
     * <p>
     * /**try {
     * URL feed = new URL("https://xkcd.com/rss.xml");
     * feed.openConnection();
     * <p>
     * SyndFeedInput input = new SyndFeedInput();
     * SyndFeed feedAllData = input.build(new XmlReader(feed));
     * <p>
     * mFeedDescription = feedAllData.getDescription();
     * mFeedTitle = feedAllData.getTitle();
     * mFeedLink = feedAllData.getLink();
     * <p>
     * RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, );
     * mList.add(rssFeedModel);
     * <p>
     * } catch (IOException e) {
     * mFeedLink = "err";
     * mFeedTitle = "err";
     * mFeedDescription = "err";
     * <p>
     * }
     * catch (FeedException e) {
     * mFeedLink = "err";
     * mFeedTitle = "err";
     * mFeedDescription = "err";
     * <p>
     * }
     * <p>
     * <p>
     * return items;
     * }
     **/


    public void about(MenuItem menuItem) {
        startActivity(new Intent(this, com.andromeda.ara.about.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onpopupuiClicked(int position) {

    }
    @RequiresApi(26)
    public String time(){

        int mHour = LocalTime.now().getHour();
        if (mHour > 12){
            mTime = "Good morning";
        }
        else{
            mTime = "Good evening";
        }
        return mTime;

    }
}

