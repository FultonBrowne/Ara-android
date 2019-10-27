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

package com.andromeda.ara.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andromeda.ara.R;
import com.andromeda.ara.feeds.Drawer;
import com.andromeda.ara.feeds.Rss;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.RecyclerTouchListener;
import com.andromeda.ara.util.RssFeedModel;
import com.andromeda.ara.util.Locl;
import com.andromeda.ara.util.TagManager;
import com.andromeda.ara.voice.VoiceMain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.auth.Auth;
import com.microsoft.appcenter.auth.SignInResult;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.data.Data;
import com.microsoft.appcenter.data.DefaultPartitions;
import com.microsoft.appcenter.utils.async.AppCenterConsumer;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class MainActivity extends AppCompatActivity {
    /**
     * these have to do with permissions
     **/
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_RECORD_AUDIO = 13;
    //this is the text for the greeting it is hello by default for compatibility reasons
    private String mTime = "hello";
    //this is the navigation Drawer
    private com.mikepenz.materialdrawer.Drawer drawer = null;
    //Adapter
    private RecyclerView.Adapter mAdapter;
    // Data set for list out put
    private List<RssFeedModel> rssFeedModel1 = new ArrayList<>();
    //RecyclerView
    private RecyclerView recyclerView;
    //Device screen width
    private int screenWidth;
    SharedPreferences mPrefs;
    String mEmail;
    String mName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCenter.start(getApplication(), "fbc54802-e5ba-4a5d-9e02-e3a5dcf4922b",
                Analytics.class, Crashes.class, Auth.class, Data.class);
        logIn();
        final TagManager main53 = new TagManager(this);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Get data stored for welcome screen

        //name of the preference
        String welcomeScreenShownPref = "welcomeScreenShown";
        boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        mName = mPrefs.getString("name", "please log in");
        mEmail = mPrefs.getString("email", "please log in");

        screenWidth = checkScreenWidth();

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
        recyclerView = findViewById(R.id.list);


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
                        new ProfileDrawerItem().withName(mName).withEmail(mEmail).withIcon(getResources().getDrawable(R.drawable.example_appwidget_preview)
                        ))
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false).withTextColorRes(R.color.md_white_1000)
                .withHeaderBackground(R.color.semi_transparent)
                .withThreeSmallProfileImages(true)

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
            editor.apply();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawer = new DrawerBuilder()
                        .withActivity(ctx)
                        .withToolbar(mActionBarToolbar)
                        .withAccountHeader(headerResult)
                        .withSliderBackgroundDrawableRes(R.drawable.drawerimage)
                        .withFullscreen(true).withTranslucentNavigationBarProgrammatically(true)
                        .withTranslucentStatusBar(true)
                        .addDrawerItems(
                                item1,
                                item2,
                                item3,
                                newsmain,
                                item4,
                                item5,
                                item6,
                                item7
                        )

                        .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                            MainActivity.this.runOnUiThread(() -> {
                                try {
                                    recyclerView.setAdapter(new Drawer().main(drawerItem.getIdentifier(), ctx, main53, MainActivity.this));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            return false;
                            // do something with the clicked item :D
                        })
                        .build();
            }
        });



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
                    Intent intent = new Intent(ctx, AllContent.class);

                    intent.putExtra("NAME", rssFeedModel1.get(position).title);
                    intent.putExtra("ACT", rssFeedModel1.get(position).description);
                    intent.putExtra("PIC", rssFeedModel1.get(position).image);

                    //startActivity(browserIntent);

                    startActivity(intent);
                }
            }


            @Override
            public void onLongClick(View view, int position) {
                insert(rssFeedModel1.get(position).title, rssFeedModel1.get(position).link, main53);

                Toast.makeText(getApplicationContext(), "Tagged", Toast.LENGTH_SHORT).show();

            }
        }));

        if (screenWidth > getResources().getInteger(R.integer.max_screen_width)) {
            checkScreenOrientation();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        ctx.runOnUiThread(() -> {
            try {
                rssFeedModel1 = (new Rss().parseRss(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAdapter = new Adapter(rssFeedModel1);

            recyclerView.setAdapter(mAdapter);

        });


        //recyclerView.setAdapter(new Adapter(parseFeed()));

        //Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // Start the recording and recognition thread
            requestMicrophonePermission();

            //String phrase = new run().run1(ctx, ctx);
            //Toast.makeText(ctx, phrase, Toast.LENGTH_LONG).show();
            //mAdapter.notifyDataSetChanged();
            Intent intent = new Intent(ctx, VoiceMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        });

    }

    private void checkScreenOrientation() {
        //GridLayoutManager
        GridLayoutManager gridLayoutManager;
        if (this.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (this.recyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }

    private int checkScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void openSettingsActivity(MenuItem menuItem) {
        startActivity(new Intent(this, SettingsActivity.class));
    }


    public void about(MenuItem menuItem) {
        startActivity(new Intent(this, About.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (screenWidth > getResources().getInteger(R.integer.max_screen_width)) {
            checkScreenOrientation();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
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
            @Contract(pure = true)
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                //RssFeedModel rssFeedModel2 = (new com.andromeda.ara.Wolfram().Wolfram1(input));
                requestLocationPermission();
                ArrayList<RssFeedModel> rssFeedModel2 = (new Search().main(query, Double.toString(Locl.longitude), Double.toString(Locl.latitude), getApplicationContext()));
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
        //handle the back press :D close the Drawer first and if the Drawer is closed close the activity
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else if (drawer != null && !drawer.isDrawerOpen()) {
            drawer.openDrawer();
        } else {
            super.onBackPressed();
        }
    }

    void insert(String main, String link, @NonNull TagManager main53) {
        main53.open();
        main53.insert(main, link);
        main53.close();
    }


    @RequiresApi(26)
    public void time() {

        int mHour = LocalTime.now().getHour();
        if (mHour < 12) {
            mTime = "Good morning";
        }
        if (mHour >= 12 && mHour < 16) {
            mTime = "good afternoon";
        } else{
            mTime = "Good evening";
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }

    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);

        }


    }


    public void logIn() {
        Auth.signIn().thenAccept(new AppCenterConsumer<SignInResult>() {


            @Override
            public void accept(SignInResult signInResult) {


                if (signInResult.getException() == null) {

                    // Sign-in succeeded.
                    try {
                        String accountId = signInResult.getUserInformation().getAccountId();
                        String idToken = signInResult.getUserInformation().getIdToken();
                        System.out.println(accountId);
                        JWT parsedToken = JWTParser.parse(idToken);
                        Map<String, Object> claims = parsedToken.getJWTClaimsSet().getClaims();
                        System.out.print("check if null");
                        net.minidev.json.JSONArray emails = (net.minidev.json.JSONArray) claims.get("emails");
                        String displayName = (String) claims.get("given_name");
                        mPrefs.edit().putString("name", displayName).apply();
                        System.out.print(displayName);
                        if (emails != null && !emails.isEmpty()) {
                            String firstEmail = emails.get(0).toString();
                            mPrefs.edit().putString("email", firstEmail).apply();
                            System.out.print(firstEmail);
                        }
                        else System.out.print("emails null");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "LOg in failed", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public void logOut(MenuItem item) {
        Auth.signOut();
    }
}