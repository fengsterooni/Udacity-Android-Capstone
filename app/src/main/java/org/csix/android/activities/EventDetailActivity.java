package org.csix.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.csix.android.fragments.EventDetailFragment;
import org.csix.android.R;

public class EventDetailActivity extends AppCompatActivity {
    private final String LOG_TAG = EventDetailActivity.class.getSimpleName();
    public final static String EVENT_ID = "EVENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        long eventID = getIntent().getLongExtra(EVENT_ID, 0);

        if (savedInstanceState == null) {
            EventDetailFragment fragment = EventDetailFragment.newInstatnce(eventID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_event_detail, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
