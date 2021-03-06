package org.csix.android.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.csix.android.R;
import org.csix.android.data.CSixContract;
import org.csix.android.utils.LocationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = GroupDetailFragment.class.getSimpleName();
    private static final String GROUP_ID = "GROUP_ID";
    private static final int LOADER_ID = 401;

    private long groupId;
    private View view;

    private static final String[] GROUP_DETAIL_COLUMNS = {
            CSixContract.GroupEntry.COLUMN_NAME,
            CSixContract.GroupEntry.COLUMN_ADDRESS,
            CSixContract.GroupEntry.COLUMN_LOCATION,
            CSixContract.GroupEntry.COLUMN_TIME,
            CSixContract.GroupEntry.COLUMN_DESC
    };

    private static final int COL_GROUP_ID       = 0;
    private static final int COL_GROUP_NAME     = 1;
    private static final int COL_GROUP_ADDRESS  = 2;
    private static final int COL_GROUP_LOCATION = 3;
    private static final int COL_GROUP_TIME     = 4;
    private static final int COL_GROUP_DESC     = 5;

    private SupportMapFragment mapFragment;

    private GoogleMap map;
    private LatLng latLng;
    String locationAddress;
    private String name;
    private String address;
    private String time;
    private String location;

    @Bind(R.id.tvGroupDetailTitle)
    TextView groupName;
    @Bind(R.id.tvGroupLocation)
    TextView groupLocation;
    @Bind(R.id.tvGroupTime)
    TextView groupTime;

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    public static GroupDetailFragment newInstatnce(Long groupId) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        if (groupId != null) {
            args.putLong(GroupDetailFragment.GROUP_ID, groupId);
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
            groupId = args.getLong(GroupDetailFragment.GROUP_ID);
            Log.i(LOG_TAG, "EVENT_ID ID " + groupId);
        }

        if (savedInstanceState != null) {
            groupId = savedInstanceState.getLong(GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group_detail, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            map = mapFragment.getMap();
        }
    }

    private void setupMap() {

        if (address != null) {
            latLng = LocationUtils.getAddress(getActivity(), address);
        }

        if (latLng != null) {
            CameraPosition cp = CameraPosition.builder().target(latLng).zoom(16).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

            map.addMarker(new MarkerOptions()
                    .position(latLng));
        }
    }


    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                CSixContract.GroupEntry.buildGroupUri(groupId),
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
                // Group Name
                groupName.setText("" + data.getString(COL_GROUP_NAME));
                // Group Time
                groupTime.setText("" + data.getString(COL_GROUP_TIME));
                // Group Location
                address = data.getString(COL_GROUP_ADDRESS);
                // groupAddress.setText("" + address);
                location = data.getString(COL_GROUP_LOCATION);
                groupLocation.setText("" + address + "\n" + location);

                break;
        }

        setupMap();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(GROUP_ID, groupId);
        super.onSaveInstanceState(outState);
    }
}
