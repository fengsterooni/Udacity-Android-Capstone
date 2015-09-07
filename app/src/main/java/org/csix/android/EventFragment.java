package org.csix.android;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventFragment.class.getSimpleName();
    private EventAdapter mEventAdapter;
    private long mInitialSelection = -1;

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

    static final int COL_EVENT_ID        = 0;
    static final int COL_EVENT_DATE      = 1;
    static final int COL_EVENT_SPEAKER   = 2;
    static final int COL_EVENT_IMAGE     = 3;
    static final int COL_EVENT_TOPIC     = 4;
    static final int COL_EVENT_DESC      = 5;
    static final int COL_EVENT_TYPE      = 6;

    @Bind(R.id.recyclerview_event)
    RecyclerView mRecyclerView;

    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        //RecyclerView.ItemDecoration itemDecoration = new
        //        DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        //mRecyclerView.addItemDecoration(itemDecoration);

        mEventAdapter = new EventAdapter(getActivity(), new EventAdapter.EventAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, EventAdapter.EventAdapterViewHolder vh) {
                ((Callback) getActivity())
                        .onItemSelected(id, vh);
            }
        });

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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mEventAdapter.swapCursor(null);
    }
}
