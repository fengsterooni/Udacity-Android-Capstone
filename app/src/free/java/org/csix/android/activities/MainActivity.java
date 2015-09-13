package org.csix.android.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import org.csix.android.R;
import org.csix.android.fragments.EventFragment;
import org.csix.android.fragments.GroupFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String MAIN_TAG = "MAIN_TAG";

    PublisherInterstitialAd mPublisherInterstitialAd;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Nullable
    @Bind(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @Bind(R.id.navigation)
    NavigationView navigation;
    @Bind(R.id.adView)
    AdView adView;

    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupDrawer();

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new EventFragment(), MAIN_TAG)
                    .commit();
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        // To make interstitial work, make sure to follow:
        // https://developers.google.com/admob/android/interstitial
        // to get the right AdUnitId and TestDeviceID (from LogCat)
        // Don't forget to update add free/paid to Google MAP API signature
        // Don't forget to update AndroidManifest.xml
        
        mPublisherInterstitialAd = new PublisherInterstitialAd(MainActivity.this);
        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                showMap();
            }
        });

        requestNewInterstitial();
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        if (drawerLayout != null)
            drawerLayout.setDrawerListener(drawerToggle);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.navEvent:
                        fragment = new EventFragment();
                        // Snackbar.make(rootLayout, "Event Event Event!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navDirection:
                        // startActivity(new Intent(MainActivity.this, DirectionActivity.class));
                        // fragment = DirectionFragment.newInstance("Direction", "arg2");
                        Snackbar.make(rootLayout, "Where where?!", Snackbar.LENGTH_SHORT).show();

                        startMap(rootLayout);

                        break;
                    case R.id.navGroup:
                        fragment = new GroupFragment();
                        // Snackbar.make(rootLayout, "GOOPS!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navAbout:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        // fragment = AboutFragment.newInstance("About", "arg2");
                        // Snackbar.make(rootLayout, "HU AM I?!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navSetting:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        // fragment = AboutFragment.newInstance("About", "arg2");
                        // Snackbar.make(rootLayout, "HU AM I?!", Snackbar.LENGTH_SHORT).show();
                        break;
                }

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment).commit();
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerLayout != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerLayout != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (drawerLayout != null)
                drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
            finish();
        }
        super.onBackPressed();
    }

    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice("E332EC9E2F6AC08446104FF1207B306E")
                .build();

        mPublisherInterstitialAd.loadAd(adRequest);
    }

    public void startMap(View view) {
        if (mPublisherInterstitialAd.isLoaded()) {
            mPublisherInterstitialAd.show();
        } else {
            showMap();
        }
    }

    public void showMap() {
        startActivity(new Intent(this, DirectionActivity.class));
    }
}
