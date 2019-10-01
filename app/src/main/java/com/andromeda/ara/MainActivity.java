/*
 * Copyright (c) 2019. Fulton Browne
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

package com.andromeda.ara;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andromeda.ara.util.GetUrlAra;
import com.andromeda.ara.util.calUtility;
import com.andromeda.ara.util.locl;
import com.andromeda.ara.util.rss;
import com.andromeda.ara.voice.run;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@SuppressLint("CutPasteId")
public class MainActivity extends AppCompatActivity {
    /** these have to do with permissions**/
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_RECORD_AUDIO = 13;
    //this is the text for the greeting it is hello by default for compatibility reasons
    private String mTime = "hello";
    //this is the navigation drawer
    private Drawer drawer = null;
    private String title1;
    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";

    private String web1;


    private RecyclerView.Adapter mAdapter;
    private List<RssFeedModel> rssFeedModel1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final tagManager main53 = new tagManager(this);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        final Activity ctx = this;
        requestLocationPermission();


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time();
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CALENDAR},
                1);


        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        RecyclerView recyclerView = findViewById(R.id.list);


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Tags").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.tag);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Food").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.food);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("Shopping").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.shop);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(5).withName("Agenda").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.ic_today_black_24dp);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(6).withName("Shortcuts").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.shortcut);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(7).withName("Devices").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.devices);
        SecondaryDrawerItem news1 = new SecondaryDrawerItem().withIdentifier(102).withName("Tech").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.technews);
        SecondaryDrawerItem news3 = new SecondaryDrawerItem().withIdentifier(104).withName("Domestic").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.domnews);
        SecondaryDrawerItem news4 = new SecondaryDrawerItem().withIdentifier(105).withName(getString(R.string.moneyText)).withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.money);
        SecondaryDrawerItem news2 = new SecondaryDrawerItem().withIdentifier(103).withName("World").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.worldnews);
        SecondaryDrawerItem newsmain = new SecondaryDrawerItem().withIdentifier(101).withName("News").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.semi_transparent).withSubItems(news1, news2, news3, news4).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.news);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                //.withHeaderBackground(R.drawable.back)


                .addProfiles(
                        new ProfileDrawerItem().withName("name").withEmail("email@gmail.com").withIcon(getResources().getDrawable(R.drawable.example_appwidget_preview)
                        ))
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false).withTextColorRes(R.color.md_white_1000)
                .withHeaderBackground(R.color.semi_transparent)

                .build();
        if (!welcomeScreenShown) {
            // here you can launch another activity if you like
            // the code below will display a popup

            String whatsNewTitle = ("Welcome to ara for android!");
            String whatsNewText = ("Thank you for downloading and investing in the next generation of intelligent voice assistants.");
            new AlertDialog.Builder(this).setTitle(whatsNewTitle).setMessage(whatsNewText).setPositiveButton(
                    getText(R.string.textOK), (dialog, which) -> dialog.dismiss()).show();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.apply(); // Very important to save the preference
        }


//Now create your drawer and pass the AccountHeader.Result


//create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mActionBarToolbar)
                .withAccountHeader(headerResult)
                //.withSliderBackgroundColorRes(R.color.colorBack)
                .withSliderBackgroundDrawableRes(R.drawable.drawerimage)
                .withFullscreen(true).withTranslucentNavigationBarProgrammatically(true)

                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        newsmain,
                        item4,
                        item5,
                        item6,
                        item7
                )

                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem.getIdentifier() == 1) {
                        Toast.makeText(getApplicationContext(), "number 1", Toast.LENGTH_SHORT).show();
                        try {
                            RecyclerView recyclerView1 = findViewById(R.id.list);

                            rssFeedModel1 = (new rss().parseRss(0));
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView1.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (drawerItem.getIdentifier() == 2) {
                        Toast.makeText(getApplicationContext(), "number 2", Toast.LENGTH_SHORT).show();

                        RecyclerView recyclerView1 = findViewById(R.id.list);
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


                        recyclerView1.setAdapter(mAdapter);


                        //recyclerView.setAdapter(new Adapter(parseFeed()));

                    } else if (drawerItem.getIdentifier() == 3) {

                        Toast.makeText(getApplicationContext(), "number 3", Toast.LENGTH_SHORT).show();
                        RecyclerView recyclerView1 = findViewById(R.id.list);


                        RssFeedModel test = new RssFeedModel("food", "zomato.com", "food near you coming soon", "");
                        rssFeedModel1.clear();
                        rssFeedModel1.add(test);


                        //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");


                        new locl(ctx);
                        rssFeedModel1 = new food().getFood(Double.toString(locl.longitude), Double.toString(locl.latitude));
                        mAdapter = new Adapter(rssFeedModel1);

                        recyclerView1.setAdapter(mAdapter);
                    } else if (drawerItem.getIdentifier() == 4) {
                        Toast.makeText(getApplicationContext(), "number 4", Toast.LENGTH_SHORT).show();
                        RecyclerView recyclerView1 = findViewById(R.id.list);

                        rssFeedModel1.clear();


                        //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");


                        new locl(ctx);
                        ArrayList<RssFeedModel> shoppingOutput = new shopping().getShops(Double.toString((locl.longitude)), Double.toString((locl.latitude)));
                        rssFeedModel1 = shoppingOutput;
                        mAdapter = new Adapter(rssFeedModel1);

                        recyclerView1.setAdapter(mAdapter);


                        //recyclerView.setAdapter(new Adapter(parseFeed()));
                    } else if (drawerItem.getIdentifier() == 5) {
                        Toast.makeText(getApplicationContext(), "number 5", Toast.LENGTH_SHORT).show();
                        RecyclerView recyclerView1 = findViewById(R.id.list);

                        rssFeedModel1.clear();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_CALENDAR},
                                1);


                        //ArrayList<RssFeedModel> main352 = new food().getFood("-122.658722","45.512230");

                        ArrayList<RssFeedModel> calenderContent = calUtility.readCalendarEvent(ctx);
                        rssFeedModel1 = calenderContent;
                        mAdapter = new Adapter(rssFeedModel1);

                        recyclerView1.setAdapter(mAdapter);


                        //recyclerView.setAdapter(new Adapter(parseFeed()));
                    } else if (drawerItem.getIdentifier() == 102) {
                        Toast.makeText(getApplicationContext(), "number 1", Toast.LENGTH_SHORT).show();
                        try {
                            RecyclerView recyclerView1 = findViewById(R.id.list);

                            rssFeedModel1 = (new rss().parseRss(2));
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView1.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (drawerItem.getIdentifier() == 101) {
                        Toast.makeText(getApplicationContext(), "number 1", Toast.LENGTH_SHORT).show();
                        try {
                            RecyclerView recyclerView1 = findViewById(R.id.list);

                            rssFeedModel1 = (new rss().parseRss(1));
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView1.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (drawerItem.getIdentifier() == 102) {
                        Toast.makeText(getApplicationContext(), "number 1", Toast.LENGTH_SHORT).show();
                        try {
                            RecyclerView recyclerView1 = findViewById(R.id.list);

                            rssFeedModel1 = (new rss().parseRss(3));
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView1.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (drawerItem.getIdentifier() == 105) {
                        Toast.makeText(getApplicationContext(), "number 1", Toast.LENGTH_SHORT).show();
                        try {
                            RecyclerView recyclerView1 = findViewById(R.id.list);

                            rssFeedModel1 = (new rss().parseRss(4));
                            mAdapter = new Adapter(rssFeedModel1);

                            recyclerView1.setAdapter(mAdapter);


                            //recyclerView.setAdapter(new Adapter(parseFeed()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    return false;
                    // do something with the clicked item :D
                })
                .build();


        Objects.requireNonNull(getSupportActionBar()).setTitle(mTime);
        StrictMode.setThreadPolicy(policy);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (drawer.getCurrentSelection() == 1) {
                    Intent browserIntent;

                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssFeedModel1.get(position).link));
                    startActivity(browserIntent);

                } else {
                    Intent intent = new Intent(ctx, allContent.class);

                    intent.putExtra("NAME", rssFeedModel1.get(position).title);
                    intent.putExtra("DESC", rssFeedModel1.get(position).description);
                    intent.putExtra("PIC", rssFeedModel1.get(position).image);

                    //startActivity(browserIntent);

                    startActivity(intent);
                }
            }


            @Override
            public void onLongClick(View view, int position) {
                insert(rssFeedModel1.get(position).title, rssFeedModel1.get(position).link, main53);

                Toast.makeText(getApplicationContext(), "tagged", Toast.LENGTH_SHORT).show();

            }
        }));
        SwipeRefreshLayout mSwipeLayout = findViewById(R.id.swipe1);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    rssFeedModel1.clear();

                    rssFeedModel1 = (new rss().parseRss(0));

                    mAdapter.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            rssFeedModel1 = (new rss().parseRss(0));
            mAdapter = new Adapter(rssFeedModel1);

            recyclerView.setAdapter(mAdapter);


            //recyclerView.setAdapter(new Adapter(parseFeed()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {


            // Start the recording and recognition thread
            requestMicrophonePermission();

            String phrase = new run().run1(ctx, ctx);
            Toast.makeText(ctx, phrase, Toast.LENGTH_LONG).show();
            mAdapter.notifyDataSetChanged();


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
                //RssFeedModel rssFeedModel2 = (new com.andromeda.ara.Wolfram().Wolfram1(input));
                requestLocationPermission();
                ArrayList<RssFeedModel> rssFeedModel2 = (new search().main(query,Double.toString(locl.longitude), Double.toString(locl.latitude)));
                rssFeedModel1.addAll(0, rssFeedModel2);
                mAdapter.notifyDataSetChanged();


                return true;
            }
        };
        assert searchView != null;
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else if (drawer != null && !drawer.isDrawerOpen()) {
            drawer.openDrawer();
        } else {
            super.onBackPressed();
        }
    }

    void insert(String main, String link, tagManager main53) {

        main53.open();
        main53.insert(main, link);
        main53.close();
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


    public void update11(MenuItem item) {
        Toast.makeText(this, "checking for update", Toast.LENGTH_LONG).show();
        try {
            String url = new GetUrlAra().getIt(new URL("https://araserver.herokuapp.com/update/0.1"));
            Intent browserIntent;
            Toast.makeText(this, "update available", Toast.LENGTH_LONG).show();

            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}










