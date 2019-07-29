package com.andromeda.ara;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements popupuiListDialogFragment.Listener {


    public SwipeRefreshLayout mSwipeLayout;
    int searchmode = 1;
    double lat;
    double log;
    String mTime = "hello";
    Toolbar mActionBarToolbar;
    private FusedLocationProviderClient fusedLocationClient;
    private Drawer result = null;
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








        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time();
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CALENDAR},
                1);


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
        result = new DrawerBuilder()
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
                        if (drawerItem.getIdentifier() == 1){
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
                        }
                        else if (drawerItem.getIdentifier() == 2){
                            Toast.makeText(getApplicationContext(), "number 2", Toast.LENGTH_SHORT).show();

                            RecyclerView recyclerView = findViewById(R.id.list);
                            main53.open();
                            //final Cursor cursor = main53.fetch();
                            Cursor  cursor = main53.fetch();
                            RssFeedModel test = new RssFeedModel( "", "", "","");




                            if( cursor != null && cursor.moveToFirst() ){
                                cursor.moveToFirst();

                                while (!cursor.isAfterLast()) {
                                    rssFeedModel1.clear();
                             title1 = cursor.getString(1);
                             web1 = cursor.getString(2);
                                    test = new RssFeedModel( title1, web1, "","");
                                    rssFeedModel1.add(test);
                                    cursor.moveToNext();}}
                            else{
                                 title1 = "nothing";
                                 web1 = "reload app";
                                test = new RssFeedModel( title1, web1, "","");
                                rssFeedModel1.add(test);



                            }
                            //RssFeedModel test = new RssFeedModel( title1, web1, "","");
                            //rssFeedModel1.clear();
                            //rssFeedModel1.add(test);
                            mAdapter = new Adapter(rssFeedModel1);


                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));

                        }
                        else if (drawerItem.getIdentifier() == 3) {
                            mode = 2;
                            Toast.makeText(getApplicationContext(), "number 3", Toast.LENGTH_SHORT).show();
                            RecyclerView recyclerView = findViewById(R.id.list);


                            RssFeedModel test = new RssFeedModel( "food", "zomato.com", "food near you coming soon","");
                            rssFeedModel1.clear();
                            rssFeedModel1.add(test);





                          //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");

                            ArrayList<RssFeedModel> main352 = new food().getFood(Double.toString(locl.longitude),Double.toString(locl.latitude));
                            rssFeedModel1 = main352;
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        }
                        else if (drawerItem.getIdentifier() == 4) {
                            mode = 3;
                            Toast.makeText(getApplicationContext(), "number 4", Toast.LENGTH_SHORT).show();
                            RecyclerView recyclerView = findViewById(R.id.list);


                            RssFeedModel test = new RssFeedModel( "food", "zomato.com", "food near you coming soon","");
                            rssFeedModel1.clear();






                            //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");

                            ArrayList<RssFeedModel> main352 = new shopping().getShops(Double.toString(locl.longitude),Double.toString(locl.latitude));
                            rssFeedModel1 = main352;
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        }
                        else if (drawerItem.getIdentifier() == 5) {
                            mode = 3;
                            Toast.makeText(getApplicationContext(), "number 5", Toast.LENGTH_SHORT).show();
                            RecyclerView recyclerView = findViewById(R.id.list);


                            RssFeedModel test = new RssFeedModel( "food", "zomato.com", "food near you coming soon","");
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
              insert(rssFeedModel1.get(position).title, rssFeedModel1.get(position).link,main53);

                Toast.makeText(getApplicationContext(), "tagged", Toast.LENGTH_SHORT).show();

            }
        }));
        mSwipeLayout = findViewById(R.id.swipe1);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    rssFeedModel1.clear();
                   List<RssFeedModel> items2= (parseFeed());
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
                popupuiListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
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
                ArrayList <RssFeedModel> rssFeedModel2 = (new search().main(query, mode));
                rssFeedModel1.addAll(0,rssFeedModel2);
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
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();}
            else if (result != null && !result.isDrawerOpen()){
                result.openDrawer();
        }
         else {
            super.onBackPressed();
        }
    }
     void insert(String main, String link, tagManager main53){

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






}










