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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.csix.android.R;
import org.csix.android.activities.GroupDetailActivity;
import org.csix.android.adapters.GroupAdapter;
import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class GroupFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = GroupFragment.class.getSimpleName();
    private GroupAdapter mGroupAdapter;
    
    private static final int LOADER_ID = 201;

    public static final String GROUP_ID = "GROUP_ID";


    private static final String[] GROUP_COLUMNS = {
            CSixContract.GroupEntry.COLUMN_NAME,
            CSixContract.GroupEntry.COLUMN_ADDRESS,
            CSixContract.GroupEntry.COLUMN_LOCATION,
            CSixContract.GroupEntry.COLUMN_TIME,
            CSixContract.GroupEntry.COLUMN_DESC
    };

    public static final int COL_GROUP_ID       = 0;
    public static final int COL_GROUP_NAME     = 1;
    public static final int COL_GROUP_ADDRESS  = 2;
    public static final int COL_GROUP_LOCATION = 3;
    public static final int COL_GROUP_TIME     = 4;
    public static final int COL_GROUP_DESC     = 5;

    @Bind(R.id.recyclerview_group)
    RecyclerView mRecyclerView;
    @Bind(R.id.recyclerview_group_empty)
    View emptyView;

    public GroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);

        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);

        mGroupAdapter = new GroupAdapter(getActivity(), new GroupAdapter.GroupAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, GroupAdapter.GroupAdapterViewHolder vh) {
                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.GROUP_ID, id);

                // http://code.tutsplus.com/tutorials/introduction-to-the-new-lollipop-activity-transitions--cms-23711
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        getActivity(),
                        // For each shared element, add to this method a new Pair item,
                        // which contains the reference of the view we are transitioning *from*,
                        // and the value of the transitionName attribute
                        new Pair<View, String>(vh.groupName, getString(R.string.transition_name_group_name))

                );

                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                //startActivity(intent);
            }
        }, emptyView);

        mRecyclerView.setAdapter(mGroupAdapter);

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
                mGroupAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGroupAdapter.swapCursor(null);
    }
}
