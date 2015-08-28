package org.csix.android;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = EventFragment.class.getSimpleName();
    private static final int LOADER_ID = 101;

    public static final String EVENT_ID = "EVENT_ID";

    @Bind(R.id.listEvents)
    ListView listEvents;
    private EventListAdapter eventListAdapter;

    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        Cursor cursor = getActivity().getContentResolver().query(
                CSixContract.EventEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                CSixContract.EventEntry.COLUMN_DATE + " ASC"  // sort order
        );

        Log.i(LOG_TAG, "SIZE OF THE CURSOR " + cursor.getCount());

        eventListAdapter = new EventListAdapter(getActivity(), cursor, 0);
        listEvents.setAdapter(eventListAdapter);

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor data = eventListAdapter.getCursor();
                if (data != null && data.moveToPosition(position)) {
                    String eventID = data.getString(data.getColumnIndex(CSixContract.EventEntry._ID));
                    ((Callback) getActivity()).onItemSelected(EVENT_ID, eventID);
                    Log.i(LOG_TAG, "CLICKED CLICKED " + eventID);
                }
            }
        });

        return view;
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
                eventListAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        eventListAdapter.swapCursor(null);
    }
}
