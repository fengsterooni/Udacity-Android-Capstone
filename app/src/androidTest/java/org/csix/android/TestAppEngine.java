package org.csix.android;

import android.test.AndroidTestCase;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.Event;

import java.io.IOException;
import java.util.List;

public class TestAppEngine extends AndroidTestCase {

    public static final String LOG_TAG = TestAppEngine.class.getSimpleName();

    private static MyApi myApiService = null;

    public TestAppEngine() {

        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                // options for running against local devappserver
                // - 10.0.2.2 is localhost's IP address in Android emulator
                // - turn off compression when running against local devappserver
                //.setRootUrl("http://10.0.2.2:8080/_ah/api/")

                // Genymotion uses 10.0.3.2
                .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        // end options for devappserver

        myApiService = builder.build();
    }

    public void testInsertEvent() throws Throwable {
        if (myApiService != null) {
            Event event = new Event();
            String topic = "Hello World!";
            event.setTopic(topic);
            Event temp = myApiService.insertEvent(event).execute();
            assertEquals(temp, event);
            List<Event> events = myApiService.listEvent().execute().getItems();
            assertEquals(events.size(), 1);
            assertEquals(topic, events.get(0).getTopic());
        }
    }
}
