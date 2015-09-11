package org.csix.android.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.csix.android.utils.DateUtils;
import org.csix.android.activities.MainActivity;
import org.csix.android.R;
import org.csix.android.data.CSixContract;
import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.Event;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class EventIntentService extends IntentService {

    private final String LOG_TAG = EventIntentService.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED =
            "org.csix.android.ACTION_DATA_UPDATED";

    private static final int EVENT_NOTIFICATION_ID = 4001;

    public EventIntentService() {
        super("EventIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
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

        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl("https://disco-task-719.appspot.com/_ah/api/");

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
        ContentValues values;
        Vector<ContentValues> vector = new Vector<>(size);
        for (int i = 0; i < size; i++) {
            values = new ContentValues();
            event = events.get(i);
            values.put(CSixContract.EventEntry.COLUMN_DATE, DateUtils.getDateLong(DateUtils.getDateFromDateTime(event.getDate())));
            values.put(CSixContract.EventEntry.COLUMN_SPEAKER, event.getSpeaker());
            values.put(CSixContract.EventEntry.COLUMN_IMAGE, event.getImage());
            values.put(CSixContract.EventEntry.COLUMN_TOPIC, event.getTopic());
            values.put(CSixContract.EventEntry.COLUMN_DESC, event.getDesc());
            values.put(CSixContract.EventEntry.COLUMN_TYPE, event.getType());

            vector.add(values);
        }

        if (vector.size() > 0) {
            ContentValues[] contentArray = new ContentValues[vector.size()];
            vector.toArray(contentArray);
            getContentResolver().bulkInsert(CSixContract.EventEntry.CONTENT_URI, contentArray);

            updateWidgets();
            notifyEvents();
        }

        Log.i(LOG_TAG, "Update Events completed. " + vector.size() + " added.");
    }

    private void notifyEvents() {

        // we'll query our contentProvider, as always
        Cursor cursor = getContentResolver().query(
                CSixContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                CSixContract.EventEntry.COLUMN_DATE + " ASC"
        );

        if (cursor.moveToFirst()) {
            String topic = cursor.getString(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_TOPIC));
            String speakerUrl = cursor.getString(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_IMAGE));
            String speaker = cursor.getString(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_SPEAKER));
            Date date = new Date(cursor.getLong(cursor.getColumnIndex(CSixContract.EventEntry.COLUMN_DATE)));

            Resources resources = getResources();

            // On Honeycomb and higher devices, we can retrieve the size of the large icon
            // Prior to that, we use a fixed size
            @SuppressLint("InlinedApi")
            int largeIconWidth = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                    ? resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_width)
                    : resources.getDimensionPixelSize(R.dimen.notification_large_icon_default);
            @SuppressLint("InlinedApi")
            int largeIconHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                    ? resources.getDimensionPixelSize(android.R.dimen.notification_large_icon_height)
                    : resources.getDimensionPixelSize(R.dimen.notification_large_icon_default);

            // Retrieve the large icon
            Bitmap largeIcon;
            try {
                largeIcon = Glide.with(this)
                        .load(speakerUrl)
                        .asBitmap()
                        .fitCenter()
                        .into(largeIconWidth, largeIconHeight).get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(LOG_TAG, "Error retrieving large icon from " + speakerUrl, e);
                largeIcon = BitmapFactory.decodeResource(resources, R.drawable.csix_logo);
            }
            String title = getString(R.string.app_name);


            // NotificationCompatBuilder is a very convenient way to build backward-compatible
            // notifications.  Just throw in some data.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setColor(resources.getColor(R.color.main_color))
                            .setSmallIcon(R.drawable.ic_stat_update)
                            .setLargeIcon(largeIcon)
                            .setContentTitle(topic)
                            .setContentText(speaker);

            // Make something interesting happen when the user clicks on the notification.
            // In this case, opening the app is sufficient.
            Intent resultIntent = new Intent(this, MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
            mNotificationManager.notify(EVENT_NOTIFICATION_ID, mBuilder.build());

        }
        cursor.close();
    }

    private void updateWidgets() {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(getPackageName());
        sendBroadcast(dataUpdatedIntent);
    }
}
