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

package com.andromeda.ara.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andromeda.ara.R;
import com.andromeda.ara.client.models.FeedModel;
import com.andromeda.ara.client.models.SkillsModel;
import com.andromeda.ara.client.search.Actions;
import com.andromeda.ara.constants.DrawerModeConstants;
import com.andromeda.ara.constants.User;
import com.andromeda.ara.feeds.Drawer;
import com.andromeda.ara.feeds.News;
import com.andromeda.ara.iot.CacheData;
import com.andromeda.ara.iot.IotRequest;
import com.andromeda.ara.models.OutputModel;
import com.andromeda.ara.models.TabModel;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.skills.ListSkills;
import com.andromeda.ara.skills.Parse;
import com.andromeda.ara.skills.RunActions;
import com.andromeda.ara.skills.SearchFunctions;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.ApiOutputToRssFeed;
import com.andromeda.ara.util.AraPopUps;
import com.andromeda.ara.util.CardOnClick;
import com.andromeda.ara.util.GetSettings;
import com.andromeda.ara.util.GetUrlAra;
import com.andromeda.ara.util.JsonParse;
import com.andromeda.ara.util.LogIn;
import com.andromeda.ara.util.RecyclerTouchListener;
import com.andromeda.ara.util.SetFeedData;
import com.andromeda.ara.util.TabAdapter;
import com.andromeda.ara.util.TagManager;
import com.andromeda.ara.voice.VoiceMain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements SearchFunctions, Actions, SetFeedData {
    /**
     * these have to do with permissions
     **/
    private static final int REQUEST_RECORD_AUDIO = 13;
    //this is the text for the greeting it is hello by default for compatibility reasons
    private String mTime = "Hello";
    //this is the navigation Drawer
    private com.mikepenz.materialdrawer.Drawer drawer = null;
    // Data set for list out put
    private ArrayList<FeedModel> feedModel1 = new ArrayList<>();
    //RecyclerView
    private RecyclerView recyclerView;
    //Device screen width
    private int screenWidth;
    private long mode = 0;
    Activity act;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions();
        new LogIn().logIn(this);
        act = this;
        System.out.println("done part 1");
        new CacheData().main(this);
        AppCenter.start(getApplication(), "fbc54802-e5ba-4a5d-9e02-e3a5dcf4922b",
                Analytics.class, Crashes.class);
        new GetSettings().starUp(this);
        final TagManager main53 = new TagManager(this);
        screenWidth = checkScreenWidth();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time();
        }
        System.out.println("prefs");

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        recyclerView = findViewById(R.id.list);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(DrawerModeConstants.HOME).withName("Home").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(DrawerModeConstants.TAGS).withName("Tags").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.tag);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(DrawerModeConstants.FOOD).withName("Food").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.food);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(DrawerModeConstants.CAL).withName("Agenda").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.ic_today_black_24dp);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(DrawerModeConstants.SHORTCUTS).withName("Shortcuts").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.shortcut);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(DrawerModeConstants.DEVICES).withName("Devices").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.devices);
        SecondaryDrawerItem item8 = new SecondaryDrawerItem().withIdentifier(DrawerModeConstants.REMINDERS).withName("Reminders").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.done);
        SecondaryDrawerItem news1 = new SecondaryDrawerItem().withIdentifier(102).withName("Tech").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.technews);
        SecondaryDrawerItem news3 = new SecondaryDrawerItem().withIdentifier(104).withName(getString(R.string.domeNews)).withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.domnews);
        SecondaryDrawerItem news4 = new SecondaryDrawerItem().withIdentifier(105).withName(getString(R.string.moneyText)).withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.money);
        SecondaryDrawerItem newsmain = new SecondaryDrawerItem().withIdentifier(101).withName("News").withTextColorRes(R.color.md_white_1000).withSelectedColorRes(R.color.card_color).withSubItems(news1, news3, news4).withSelectedTextColorRes(R.color.md_white_1000).withIcon(R.drawable.news);
        System.out.println("items");
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                //.withHeaderBackground(R.drawable.back)
                .addProfiles(
                        new ProfileDrawerItem().withName(User.INSTANCE.getName()).withEmail(User.INSTANCE.getEmail()))

                .withOnAccountHeaderListener((view, profile, currentProfile) -> false).withTextColorRes(R.color.md_white_1000)
                .withThreeSmallProfileImages(true)

                .build();
        runOnUiThread(() -> drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mActionBarToolbar)
                .withAccountHeader(headerResult)
                .withSliderBackgroundColorRes(R.color.card_color)
                .withFullscreen(true).withTranslucentNavigationBarProgrammatically(true)
                .withTranslucentStatusBar(true)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        newsmain,
                        item5,
                        item8,
                        item6,
                        item7
                )

                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    MainActivity.this.runOnUiThread(() -> {
                        RecyclerView tabs = findViewById(R.id.tabs);
                        tabs.setVisibility(View.INVISIBLE);
                        if (drawerItem.getIdentifier() == DrawerModeConstants.DEVICES) {
                            feedModel1 = IotRequest.INSTANCE.parseAllAsFeed();
                            recyclerView.setAdapter(new Adapter(feedModel1, this));
                            mode = drawerItem.getIdentifier();
                        } else if (drawerItem.getIdentifier() == DrawerModeConstants.SHORTCUTS) {
                            System.out.println("shortcuts");
                            feedModel1 = new ListSkills().main();
                            recyclerView.setAdapter(new Adapter(feedModel1, act));
                            mode = drawerItem.getIdentifier();


                        } else {
                            try {
                                new Drawer().main(drawerItem.getIdentifier(), this, main53, feedModel1, this::setData);
                                recyclerView.setAdapter(new Adapter(feedModel1, this));
                                mode = drawerItem.getIdentifier();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    return false;
                    // do something with the clicked item :D
                })
                .build());

        System.out.println("drawer");
        Objects.requireNonNull(getSupportActionBar()).setTitle(mTime);
        StrictMode.setThreadPolicy(policy);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    Adapter adapter = (Adapter) recyclerView.getAdapter();
                    assert adapter != null;
                    new CardOnClick().mainFun(mode, adapter.getMFeedModels().get(position).getLink(), act, getApplicationContext(), MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onLongClick(View view, int position) {
                Adapter adapter = (Adapter) recyclerView.getAdapter();
                assert adapter != null;
                new CardOnClick().longClick( adapter.getMFeedModels().get(position), getApplicationContext(), main53, mode, act);
            }
        }));
        System.out.println("pre feed");
        feedModel1 = (new News().newsGeneral(this));
        System.out.println("feed done");
        Adapter mAdapter = new Adapter(feedModel1, this);
        recyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> launchVoice());

    }

    private void launchVoice() {
        // Start the recording and recognition thread
        requestMicrophonePermission();
        Intent intent = new Intent(this, VoiceMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(dialog -> requestPermissions(
                            new String[]
                                    {Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_CALENDAR}
                            , 555));
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION},
                            123);
                }
            }
        }
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
        startActivity(new Intent(this, PrefsActivity.class));


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
        runOnUiThread(() -> {
            System.out.println("menu 1");
            getMenuInflater().inflate(R.menu.menu_main, menu);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search)
                    .getActionView();
            assert searchManager != null;
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);


            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    // this is your adapter that will be filtered
                    return true;
                }
                public boolean onQueryTextSubmit(String query) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                        assert locationManager != null;
                    }
                    try {
                        new Search().main(query, MainActivity.this, MainActivity.this, null, feedModel1, MainActivity.this, MainActivity.this::setData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        });
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

    @RequiresApi(26)
    public void time() {

        int mHour = LocalTime.now().getHour();
        if (mHour < 12) {
            mTime = "Good morning";
        } else if (mHour < 18) {
            mTime = "good afternoon";
        } else {
            mTime = "Good evening";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);

        }


    }

    public void logOut(MenuItem item) {
        getSharedPreferences("auth", 0).edit().putString("authData", "").apply();
    }

    public void addSkill(MenuItem item) {
        new AraPopUps().newSkill(this);
    }




    @Override
    public void callBack(@NotNull String m, @NotNull String link) {
        new AraPopUps().textSearchResponse(this, m,link, this, this, recyclerView);
    }

    @NotNull
    @Override
    public String callForString(@NotNull String m) {
        return new AraPopUps().getDialogValueBack(this, m);
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
        RecyclerView tabs = findViewById(R.id.tabs);
        tabs.setVisibility(View.VISIBLE);
        tabs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        tabs.setAdapter(new TabAdapter(data, this));

    }

    public void newReminder(MenuItem item) {
        new AraPopUps().newReminder(this);
    }

    @Override
    public <T> T parseYaml(@NotNull String s) {
        return (T) new Parse().yamlArrayToObject(s, SkillsModel.class);
    }


    @Override
    public void runActions(@NotNull ArrayList<SkillsModel> action, @NotNull String term) {
        new RunActions().doIt(action, term, this, this, this);

    }

    @Override
    public void setData(@NotNull ArrayList<FeedModel> feedModel) {
        runOnUiThread(() -> {
            System.out.println(feedModel);
            recyclerView.setAdapter(new Adapter(feedModel, this));

        });
    }
}