package org.csix.android;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = GroupDetailFragment.class.getSimpleName();
    public static final String GROUP_ID = "GROUP_ID";
    public static final int LOADER_ID = 401;

    private String groupId;
    private View view;

    SupportMapFragment mapFragment;

    GoogleMap map;
    LatLng latLng;
    String locationAddress;
    private String name;
    private String address;
    private String time;
    private String location;

    @Bind(R.id.tvGroupDetailTitle)
    TextView groupName;
    @Bind(R.id.tvGroupAddress)
    TextView groupAddress;
    @Bind(R.id.tvGroupLocation)
    TextView groupLocation;
    @Bind(R.id.tvGroupTime)
    TextView groupTime;
    @Bind(R.id.ivGroupLocation)
    ImageView ivLocation;
    @Bind(R.id.ivGroupDetailCalendar)
    ImageView ivCalendar;

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    public static GroupDetailFragment newInstatnce(String groupId) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle args = new Bundle();
        if (groupId != null) {
            args.putString(GroupDetailFragment.GROUP_ID, groupId);
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
            groupId = args.getString(GroupDetailFragment.GROUP_ID);
            Log.i(LOG_TAG, "EVENT_ID ID " + groupId);
        }

        if (savedInstanceState != null) {
            groupId = savedInstanceState.getString(GROUP_ID);
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

            // setupMap();
        }
    }

    public void setupMap() {

        if (address != null) {
            latLng = LocationUtils.getAddress(getActivity(), address);
        }

        if (latLng != null) {
            CameraPosition cp = CameraPosition.builder().target(latLng).zoom(17).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

            map.addMarker(new MarkerOptions()
                    .position(latLng));
        }
    }


    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                CSixContract.GroupEntry.buildGroupUri(Long.parseLong(groupId)),
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

                name = data.getString(data.getColumnIndex(CSixContract.GroupEntry.COLUMN_NAME));
                groupName.setText("" + name);
                address = data.getString(data.getColumnIndex(CSixContract.GroupEntry.COLUMN_ADDRESS));
                groupAddress.setText("" + address);
                location = data.getString(data.getColumnIndex(CSixContract.GroupEntry.COLUMN_LOCATION));
                groupLocation.setText("" + location);
                time = data.getString(data.getColumnIndex(CSixContract.GroupEntry.COLUMN_TIME));
                groupTime.setText("" + time);


                break;
        }

        setupMap();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public void onPause() {
        super.onDestroyView();
        if (MainActivity.IS_TABLET && view.findViewById(R.id.fragment_event_detail) == null) {
            getFragmentManager().popBackStack();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GROUP_ID, groupId);
        super.onSaveInstanceState(outState);
    }
}
