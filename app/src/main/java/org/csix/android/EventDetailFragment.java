package org.csix.android;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = EventDetailFragment.class.getSimpleName();
    public static final String EVENT_ID = "EVENT_ID";
    public static final int LOADER_ID = 201;

    private long eventId;
    private View view;

    private static final String[] EVENT_DETAIL_COLUMNS = {
            CSixContract.EventEntry.COLUMN_DATE,
            CSixContract.EventEntry.COLUMN_SPEAKER,
            CSixContract.EventEntry.COLUMN_IMAGE,
            CSixContract.EventEntry.COLUMN_TOPIC,
            CSixContract.EventEntry.COLUMN_DESC,
            CSixContract.EventEntry.COLUMN_TYPE
    };

    static final int COL_EVENT_ID        = 0;
    static final int COL_EVENT_DATE      = 1;
    static final int COL_EVENT_SPEAKER   = 2;
    static final int COL_EVENT_IMAGE     = 3;
    static final int COL_EVENT_TOPIC     = 4;
    static final int COL_EVENT_DESC      = 5;
    static final int COL_EVENT_TYPE      = 6;

    @Bind(R.id.ivSpeaker)
    ImageView speakerImage;
    @Bind(R.id.tvSpeaker)
    TextView speaker;
    @Bind(R.id.tvTopic)
    TextView topic;
    @Bind(R.id.tvDesc)
    TextView desc;

    public EventDetailFragment() {}

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
            Log.i(LOG_TAG, "EVENT_ID ID " + eventId);
        }

        if (savedInstanceState != null) {
            eventId = savedInstanceState.getLong(EVENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);

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
                CSixContract.EventEntry.COLUMN_DATE + " ASC"
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

                speaker.setText("" + data.getString(COL_EVENT_SPEAKER));
                topic.setText("" + data.getString(COL_EVENT_TOPIC));
                desc.setText("" + data.getString(COL_EVENT_DESC));

                String imageUrl = data.getString(COL_EVENT_IMAGE);
                if (imageUrl != null) {
                    // Picasso.with(context).load(imageUrl).into(viewHolder.speakerImage);
                    Glide.with(getActivity()).load(imageUrl).into(speakerImage);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onPause() {
        super.onDestroyView();
        if (MainActivity.IS_TABLET && view.findViewById(R.id.fragment_detail) == null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(EVENT_ID, eventId);
        super.onSaveInstanceState(outState);
    }
}
