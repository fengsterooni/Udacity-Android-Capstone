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
public class GroupFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = GroupFragment.class.getSimpleName();
    private static final int LOADER_ID = 201;

    public static final String GROUP_ID = "GROUP_ID";

    @Bind(R.id.listGroups)
    ListView listGroups;
    private GroupListAdapter groupListAdapter;

    public GroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        Cursor cursor = getActivity().getContentResolver().query(
                CSixContract.GroupEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null// sort order
        );

        Log.i(LOG_TAG, "SIZE OF THE CURSOR " + cursor.getCount());

        groupListAdapter = new GroupListAdapter(getActivity(), cursor, 0);
        listGroups.setAdapter(groupListAdapter);

        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor data = groupListAdapter.getCursor();
                if (data != null && data.moveToPosition(position)) {
                    String groupID = data.getString(data.getColumnIndex(CSixContract.GroupEntry._ID));
                    ((Callback) getActivity()).onItemSelected(GROUP_ID, groupID);
                    Log.i(LOG_TAG, "CLICKED CLICKED " + groupID);
                }
            }
        });

        return view;
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                CSixContract.GroupEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                groupListAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        groupListAdapter.swapCursor(null);
    }
}
