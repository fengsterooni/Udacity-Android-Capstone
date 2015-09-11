package org.csix.android.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.csix.android.R;
import org.csix.android.adapters.AboutAdapter;
import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;



public class AboutFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = AboutFragment.class.getSimpleName();
    private AboutAdapter mAboutAdapter;
    private long aboutId;

    private static final int LOADER_ID = 201;

    public static final String ABOUT_ID = "ABOUT_ID";


    private static final String[] ABOUT_COLUMNS = {
            CSixContract.AboutEntry.COLUMN_TITLE,
            CSixContract.AboutEntry.COLUMN_DESC
    };

    public static final int COL_ABOUT_ID       = 0;
    public static final int COL_ABOUT_TITLE    = 1;
    public static final int COL_ABOUT_DESC     = 2;

    @Bind(R.id.recyclerview_about)
    RecyclerView mRecyclerView;

    public AboutFragment() {
    }

    public static AboutFragment newInstatnce(Long aboutId) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        if (aboutId != null) {
            args.putLong(AboutFragment.ABOUT_ID, aboutId);
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
            aboutId = args.getLong(AboutFragment.ABOUT_ID);
            Log.i(LOG_TAG, "EVENT_ID ID " + aboutId);
        }

        if (savedInstanceState != null) {
            aboutId = savedInstanceState.getLong(ABOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mAboutAdapter = new AboutAdapter();

        mRecyclerView.setAdapter(mAboutAdapter);

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
                CSixContract.AboutEntry.CONTENT_URI,
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
                mAboutAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAboutAdapter.swapCursor(null);
    }
}