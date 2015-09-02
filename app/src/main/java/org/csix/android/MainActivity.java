package org.csix.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EVENTDETAIL_TAG = "EVENTDETAIL_TAG";
    public static final String GROUPDETAIL_TAG = "GROUPDETAIL_TAG";
    public static final String MAIN_TAG = "MAIN_TAG";
    public static boolean IS_TABLET = false;

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

    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_TABLET = isTablet();
        if (IS_TABLET) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main);
        }
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupDrawer();


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new EventFragment(), MAIN_TAG)
                    .commit();

            startService(new Intent(this, EventIntentService.class));
            startService(new Intent(this, GroupIntentService.class));
            startService(new Intent(this, AboutIntentService.class));
        }
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
                        startActivity(new Intent(MainActivity.this, DirectionActivity.class));
                        // fragment = DirectionFragment.newInstance("Direction", "arg2");
                        // Snackbar.make(rootLayout, "Where where?!", Snackbar.LENGTH_SHORT).show();
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
                }

                if (fragment != null) {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment);

                    // Remove the leftover detailed fragment if master is changed
                    if (IS_TABLET) {
                        FragmentManager manager = getSupportFragmentManager();
                        Fragment frag;
                        if ((frag = manager.findFragmentByTag(EVENTDETAIL_TAG)) != null)
                            ft.remove(frag);
                        if ((frag = manager.findFragmentByTag(GROUPDETAIL_TAG)) != null)
                            ft.remove(frag);

                    }

                    fragment = null;

                    ft.commit();
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

        if (id == android.R.id.home) {
            if (drawerLayout != null)
                drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(long id, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof EventAdapter.EventAdapterViewHolder) {
            if (IS_TABLET) {
                EventDetailFragment fragment = EventDetailFragment.newInstatnce(id);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, fragment, EVENTDETAIL_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EVENT_ID, id);
                startActivity(intent);
            }
        }

        if (viewHolder instanceof GroupAdapter.GroupAdapterViewHolder) {
            if (IS_TABLET) {
                GroupDetailFragment fragment = GroupDetailFragment.newInstatnce(id);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, fragment, GROUPDETAIL_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.GROUP_ID, id);
                startActivity(intent);
            }
        }
    }
}
