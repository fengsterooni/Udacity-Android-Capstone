package org.csix.android.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.csix.android.data.CSixContract;
import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.Group;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class GroupIntentService extends IntentService {

    private final String LOG_TAG = GroupIntentService.class.getSimpleName();
    public static final String FETCH_GROUP = "org.csix.services.action.FETCH_GROUP";

    public GroupIntentService() {
        super("GroupIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
        // This part is for local backend testing
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                // options for running against local devappserver
                // - 10.0.2.2 is localhost's IP address in Android emulator
                // - 10.0.3.2 is localhost's IP address in Genymotion emulator
                // - turn off compression when running against local devappserver
                .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                }); */
        // end options for devappserver

        // This part is for deployed Google Endpoint
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl("https://disco-task-719.appspot.com/_ah/api/");

        MyApi myApiService = builder.build();
        try {
            List<Group> groups = myApiService.listGroup().execute().getItems();
            if (groups != null) {
                Log.i(LOG_TAG, groups.toString());
                getGroupData(groups);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getGroupData(List<Group> groups) {
        Group group;
        int size = groups.size();
        ContentValues values;
        Vector<ContentValues> vector = new Vector<>(size);
        for (int i = 0; i < size; i++) {
            values = new ContentValues();
            group = groups.get(i);
            values.put(CSixContract.GroupEntry.COLUMN_NAME, group.getName());
            values.put(CSixContract.GroupEntry.COLUMN_ADDRESS, group.getAddress());
            values.put(CSixContract.GroupEntry.COLUMN_LOCATION, group.getLocation());
            values.put(CSixContract.GroupEntry.COLUMN_TIME, group.getTime());
            values.put(CSixContract.GroupEntry.COLUMN_DESC, group.getDesc());

            vector.add(values);
        }

        if (vector.size() > 0) {
            ContentValues[] contentArray = new ContentValues[vector.size()];
            vector.toArray(contentArray);
            getContentResolver().bulkInsert(CSixContract.GroupEntry.CONTENT_URI, contentArray);
        }

        Log.i(LOG_TAG, "Update Groups completed. " + vector.size() + " added.");
    }
}
