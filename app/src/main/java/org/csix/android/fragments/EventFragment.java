package org.csix.android.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.csix.android.R;
import org.csix.android.activities.EventDetailActivity;
import org.csix.android.adapters.EventAdapter;
import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventFragment.class.getSimpleName();
    private EventAdapter mEventAdapter;

    private static final int LOADER_ID = 101;
    public static final String EVENT_ID = "EVENT_ID";

    private static final String[] EVENT_COLUMNS = {
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

    @Bind(R.id.recyclerview_event)
    RecyclerView mRecyclerView;
    @Bind(R.id.recyclerview_event_empty)
    View emptyView;

    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mEventAdapter = new EventAdapter(getActivity(), new EventAdapter.EventAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, EventAdapter.EventAdapterViewHolder vh) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EVENT_ID, id);

                // http://code.tutsplus.com/tutorials/introduction-to-the-new-lollipop-activity-transitions--cms-23711
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        getActivity(),
                        // For each shared element, add to this method a new Pair item,
                        // which contains the reference of the view we are transitioning *from*,
                        // and the value of the transitionName attribute
                        new Pair<View, String>(vh.speakerImage, getString(R.string.transition_name_profile))
                        // ,
                        // new Pair<View, String>(vh.topic, getString(R.string.transition_name_topic)),
                        // new Pair<View, String>(vh.speaker, getString(R.string.transition_name_speaker))
                );
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                // startActivity(intent);
            }
        }, emptyView);

        mRecyclerView.setAdapter(mEventAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                CSixContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                CSixContract.EventEntry.COLUMN_DATE + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case LOADER_ID:
                mEventAdapter.swapCursor(data);
                break;
        }

        Log.i(LOG_TAG, "SIZE OF THE CURSOR: " + mEventAdapter.getItemCount());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventAdapter.swapCursor(null);
    }
}
