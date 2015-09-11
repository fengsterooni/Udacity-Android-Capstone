package org.csix.android.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.csix.android.R;
import org.csix.android.activities.DirectionActivity;
import org.csix.android.activities.EventDetailActivity;
import org.csix.android.data.CSixContract;
import org.csix.android.utils.CalendarUtils;
import org.csix.android.utils.DateUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = EventDetailFragment.class.getSimpleName();
    public static final String EVENT_ID = "EVENT_ID";
    public static final int LOADER_ID = 201;

    private long eventId;
    private View view;
    private Cursor mCursor;
    private Date date;
    private String eventTopic, eventSpeaker, dateTime;

    private static final String[] EVENT_DETAIL_COLUMNS = {
            CSixContract.EventEntry.COLUMN_DATE,
            CSixContract.EventEntry.COLUMN_SPEAKER,
            CSixContract.EventEntry.COLUMN_IMAGE,
            CSixContract.EventEntry.COLUMN_TOPIC,
            CSixContract.EventEntry.COLUMN_DESC,
            CSixContract.EventEntry.COLUMN_TYPE
    };

    public static final int COL_EVENT_ID = 0;
    public static final int COL_EVENT_DATE = 1;
    public static final int COL_EVENT_SPEAKER = 2;
    public static final int COL_EVENT_IMAGE = 3;
    public static final int COL_EVENT_TOPIC = 4;
    public static final int COL_EVENT_DESC = 5;
    public static final int COL_EVENT_TYPE = 6;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Nullable
    @Bind(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @Bind(R.id.share_fab)
    FloatingActionButton fab;

    @Nullable
    @OnClick(R.id.share_fab)
    void click() {
        String summary = getSummary().toString();
        Log.i(LOG_TAG, summary);
        if (summary != null)
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(summary)
                    .getIntent(), getString(R.string.action_share)));
    }

    @Bind(R.id.ivSpeaker)
    ImageView speakerImage;
    @Bind(R.id.tvSpeaker)
    TextView speaker;
    @Bind(R.id.tvTopic)
    TextView topic;
    @Bind(R.id.tvDesc)
    TextView desc;
    @Bind(R.id.tvEventDetailTime)
    TextView time;
    @Bind(R.id.ivEventDetailCalendar)
    ImageView calendar;
    @OnClick(R.id.ivEventDetailCalendar)
    void addToCalendar() {
        CalendarUtils.addToCalendar(getContext(), eventTopic, date);
    }
    @Bind(R.id.ivEventDetailLocation)
    ImageView location;
    @OnClick(R.id.ivEventDetailLocation)
    void locationAddress() {
        startActivity(new Intent(getActivity(), DirectionActivity.class));
    }
    @Bind(R.id.tvEventDetailLocation)
    TextView address;

    public EventDetailFragment() {
    }

    public static EventDetailFragment newInstatnce(Long eventId) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        if (eventId != null) {
            args.putLong(EventDetailFragment.EVENT_ID, eventId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            eventId = args.getLong(EventDetailFragment.EVENT_ID);
            Log.i(LOG_TAG, "EVENT_ID ID in Args " + eventId);
        }

        if (savedInstanceState != null) {
            eventId = savedInstanceState.getLong(EVENT_ID);
            Log.i(LOG_TAG, "EVENT_ID ID in saved State " + eventId);
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);

        ((EventDetailActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((EventDetailActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                CSixContract.EventEntry.buildEventUri(eventId),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        switch (loader.getId()) {
            case LOADER_ID:
                Log.i(LOG_TAG, data.toString());

                mCursor = data;
                date = new Date(data.getLong(COL_EVENT_DATE));

                Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/RobotoCondensed-Light.ttf");

                speaker.setTypeface(font);
                eventSpeaker = data.getString(COL_EVENT_SPEAKER);
                speaker.setText("" + eventSpeaker);

                eventTopic = data.getString(COL_EVENT_TOPIC);
                topic.setText("" + eventTopic);

                desc.setText("" + data.getString(COL_EVENT_DESC));

                String imageUrl = data.getString(COL_EVENT_IMAGE);
                if (imageUrl != null) {
                    // Picasso.with(context).load(imageUrl).into(viewHolder.speakerImage);
                    Glide.with(getActivity()).load(imageUrl).into(speakerImage);
                }

                dateTime = DateUtils.getShortMonthString(date)
                        + " "
                        + DateUtils.getDayString(date)
                        + " @ "
                        + getString(R.string.main_event_start)
                        + " - "
                        + getString(R.string.main_event_end);
                time.setText(dateTime);

                address.setText(
                        getResources().getString(R.string.main_event_location)
                        + "\n"
                        +
                        getResources().getString(R.string.main_event_address));

                break;
        }
    }

    public StringBuilder getSummary() {
        StringBuilder summary = new StringBuilder();
        return summary
                .append(eventSpeaker)
                .append(": ")
                .append(eventTopic)
                .append(", ")
                .append(dateTime)
                .append(" @CSix Connect - www.csix.org");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(EVENT_ID, eventId);
        super.onSaveInstanceState(outState);
    }
}
