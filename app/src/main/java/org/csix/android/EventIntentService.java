package org.csix.android;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.csix.android.data.CSixContract;
import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.Event;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class EventIntentService extends IntentService {

    private final String LOG_TAG = EventIntentService.class.getSimpleName();
    public static final String FETCH_EVENT = "org.csix.services.action.FETCH_EVENT";

    public EventIntentService() {
        super("EventIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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
                });
        // end options for devappserver

        MyApi myApiService = builder.build();
        try {
            // ArrayList<Event> events = myApiService.listEvent();
            List<Event> events = myApiService.listEvent().execute().getItems();
            if (events != null) {
                Log.i(LOG_TAG, events.toString());
                getEventData(events);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEventData(List<Event> events) {
        Event event;
        int size = events.size();
        ContentValues values = new ContentValues();
        Vector<ContentValues> vector = new Vector<>(size);
        for (int i = 0; i < size; i++) {
            event = events.get(i);
            values.put(CSixContract.EventEntry.COLUMN_DATE, DateUtils.getDateLong(DateUtils.getDateFromDateTime(event.getDate())));
            values.put(CSixContract.EventEntry.COLUMN_SPEAKER, event.getSpeaker());
            values.put(CSixContract.EventEntry.COLUMN_TOPIC, event.getTopic());
            values.put(CSixContract.EventEntry.COLUMN_DESC, event.getDesc());
            values.put(CSixContract.EventEntry.COLUMN_TYPE, event.getType());

            vector.add(values);
        }

        if (vector.size() > 0) {
            ContentValues[] contentArray = new ContentValues[vector.size()];
            vector.toArray(contentArray);
            getContentResolver().bulkInsert(CSixContract.EventEntry.CONTENT_URI, contentArray);
        }

        Log.i(LOG_TAG, "Update Events completed. " + vector.size() + " added.");
    }
}
