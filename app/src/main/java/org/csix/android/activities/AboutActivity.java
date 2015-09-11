package org.csix.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.csix.android.fragments.AboutFragment;
import org.csix.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {
    private final String LOG_TAG = AboutActivity.class.getSimpleName();
    static AboutFragment fragment;
    public final static String ABOUT_ID = "ABOUT_ID";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        long aboutID = getIntent().getLongExtra(ABOUT_ID, 0);

        if (savedInstanceState == null) {
            fragment = AboutFragment.newInstatnce(aboutID);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_about, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                // overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
