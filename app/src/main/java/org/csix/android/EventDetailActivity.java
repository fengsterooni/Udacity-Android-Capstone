package org.csix.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity {
    static EventDetailFragment fragment;
    public final static String EVENT_ID = "EVENT_ID";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @Bind(R.id.share_fab)
    FloatingActionButton fab;
    @OnClick(R.id.share_fab)
    void click() {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText("Some sample text")
                .getIntent(), getString(R.string.action_share)));
    }

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
