package org.csix.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {
    static EventDetailFragment fragment;
    public final static String EVENT_ID = "EVENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        String eventID = getIntent().getStringExtra(EVENT_ID);

        if (savedInstanceState == null) {
            fragment = EventDetailFragment.newInstatnce(eventID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_event_detail, fragment, MainActivity.EVENTDETAIL_TAG)
                    .commit();
        }
    }
}
