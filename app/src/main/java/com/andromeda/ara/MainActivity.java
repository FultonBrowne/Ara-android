package com.andromeda.ara;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.andromeda.ara.dummy.DummyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.TimeUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.net.URL;
import java.io.InputStreamReader;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;


import android.os.StrictMode;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.UiModeManager.MODE_NIGHT_YES;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class MainActivity extends AppCompatActivity implements popupuiListDialogFragment.Listener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public SwipeRefreshLayout mSwipeLayout;
    public List<RssFeedModel> mFeedModelList;
    public TextView mFeedTitleTextView;
    public TextView mFeedLinkTextView;
    public TextView mFeedDescriptionTextView;
    public String mFeedTitle;
    public String mFeedLink;
    public String mFeedDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SharedPreferences prefs = getSharedPreferences("com.andromeda.ara.SettingActivity", MODE_PRIVATE);
        String prefs2 = prefs.getString("example_list", "MODE_PRIVATE");



        recyclerView = (RecyclerView) findViewById(R.id.list);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(new Adapter(parseFeed()));


        //theme(prefs2);


        //recyclerView.setAdapter(new Adapter(mFeedModelList));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbarthing);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Speak now", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                popupuiListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
            }
        });

    }

    public void yourMethodName(MenuItem menuItem) {
        startActivity(new Intent(this, com.andromeda.ara.SettingsActivity.class));
    }

   /** public List<RssFeedModel> parseFeed() {


        String[] item1 = {
                "infotest1"
        };
        String[] item2 = {
                "infotest1"
        };
        String[] item3 = {
                "infotest1"
        };


        /**try {
         URL feed = new URL("https://xkcd.com/rss.xml");
         feed.openConnection();

         SyndFeedInput input = new SyndFeedInput();
         SyndFeed feedAllData = input.build(new XmlReader(feed));

         mFeedDescription = feedAllData.getDescription();
         mFeedTitle = feedAllData.getTitle();
         mFeedLink = feedAllData.getLink();

         RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, );
         mList.add(rssFeedModel);

         } catch (IOException e) {
         mFeedLink = "err";
         mFeedTitle = "err";
         mFeedDescription = "err";

         }
         catch (FeedException e) {
         mFeedLink = "err";
         mFeedTitle = "err";
         mFeedDescription = "err";

         }


        return items;
    }**/


    public void about(MenuItem menuItem) {
        startActivity(new Intent(this, com.andromeda.ara.about.class));
    }

    public void theme(String theme) {
        if (theme == "Dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (theme == "Light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        } else if (theme == "Battery saver") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
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
    public static List<RssFeedModel> parseFeed() {
         String mFeedTitle;
         String mFeedLink;
         String mFeedDescription;
        List<RssFeedModel> items = new ArrayList<>();
        try{
            URL feed = new URL("https://xkcd.com/rss.xml");
            feed.openConnection();

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feedAllData = input.build(new XmlReader(feed));

             mFeedDescription = feedAllData.getDescription();
             mFeedTitle = feedAllData.getTitle();
             mFeedLink = feedAllData.getLink();




    } catch (IOException e) {
        mFeedLink = "err";
        mFeedTitle = "err";
        mFeedDescription = "err";

    }
               catch (FeedException e) {
        mFeedLink = "err";
        mFeedTitle = "err";
        mFeedDescription = "err";

    }
        RssFeedModel rssFeedModel = new RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle);
        items.add(rssFeedModel);
        return items;


}
}

