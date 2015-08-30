package org.csix.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GroupDetailActivity extends AppCompatActivity {
    static GroupDetailFragment fragment;
    public final static String GROUP_ID = "GROUP_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        long groupID = getIntent().getLongExtra(GROUP_ID, 0);

        if (savedInstanceState == null) {
            fragment = GroupDetailFragment.newInstatnce(groupID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_group_detail, fragment, MainActivity.EVENTDETAIL_TAG)
                    .commit();
        }
    }
}
