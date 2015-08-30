package org.csix.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        EventsFragment.Callback,
        GroupsFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String EVENTDETAIL_TAG = "EVENTDETAIL_TAG";
    public static final String GROUPDETAIL_TAG = "GROUPDETAIL_TAG";
    public static boolean IS_TABLET = false;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
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
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawer();


        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new EventsFragment())
                    .commit();

            startService(new Intent(this, EventIntentService.class));
            startService(new Intent(this, GroupIntentService.class));

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

        drawerLayout.setDrawerListener(drawerToggle);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.navEvent:
                        fragment = new EventsFragment();
                        // Snackbar.make(rootLayout, "Event Event Event!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navDirection:
                        startActivity(new Intent(MainActivity.this, DirectionActivity.class));
                        // fragment = DirectionFragment.newInstance("Direction", "arg2");
                        // Snackbar.make(rootLayout, "Where where?!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navGroup:
                        fragment = new GroupsFragment();
                        // Snackbar.make(rootLayout, "GOOPS!", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navAbout:
                        // fragment = AboutFragment.newInstance("About", "arg2");
                        Snackbar.make(rootLayout, "HU AM I?!", Snackbar.LENGTH_SHORT).show();
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }

                drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    /*
    @Override
    public void onItemSelected(String tag, String itemID) {
        if (tag.equals(EventFragment.EVENT_ID)) {
            if (IS_TABLET) {
                EventDetailFragment fragment = EventDetailFragment.newInstatnce(itemID);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, fragment, EVENTDETAIL_TAG)
                                // .addToBackStack("Event Detail")
                        .commit();
            } else {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EVENT_ID, itemID);
                startActivity(intent);
            }
        } else if (tag.equals(GroupFragment.GROUP_ID)) {
            if (IS_TABLET) {
                GroupDetailFragment fragment = GroupDetailFragment.newInstatnce(itemID);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail, fragment, GROUPDETAIL_TAG)
                                // .addToBackStack("Event Detail")
                        .commit();
            } else {
                Intent intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.GROUP_ID, itemID);
                startActivity(intent);
            }
        }
    }
    */

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(Long eventId, EventAdapter.EventAdapterViewHolder vh) {

        if (IS_TABLET) {
            EventDetailFragment fragment = EventDetailFragment.newInstatnce(eventId.toString());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail, fragment, EVENTDETAIL_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra(EventDetailActivity.EVENT_ID, eventId.toString());
            startActivity(intent);
        }

    }

    @Override
    public void onItemSelected(Long groupId, GroupAdapter.GroupAdapterViewHolder vh) {

        if (IS_TABLET) {
            GroupDetailFragment fragment = GroupDetailFragment.newInstatnce(groupId.toString());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail, fragment, GROUPDETAIL_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, GroupDetailActivity.class);
            intent.putExtra(GroupDetailActivity.GROUP_ID, groupId.toString());
            startActivity(intent);
        }
    }
}
