package org.csix.android;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity {
    static EventDetailFragment fragment;
    public final static String EVENT_ID = "EVENT_ID";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.rootLayout)
    CoordinatorLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        long eventID = getIntent().getLongExtra(EVENT_ID, 0);

        if (savedInstanceState == null) {
            fragment = EventDetailFragment.newInstatnce(eventID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_event_detail, fragment, MainActivity.EVENTDETAIL_TAG)
                    .commit();
        }
    }
}
