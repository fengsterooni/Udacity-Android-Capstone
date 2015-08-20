package org.csix.android;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.Event;

import java.io.IOException;
import java.util.List;

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
            Log.i(LOG_TAG, events.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
