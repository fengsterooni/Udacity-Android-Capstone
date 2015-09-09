package org.csix.android.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.RemoteViews;

import org.csix.android.DateUtils;
import org.csix.android.MainActivity;
import org.csix.android.R;
import org.csix.android.data.CSixContract;

import java.util.Date;

public class EventWidgetIntentService extends IntentService implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] EVENT_COLUMNS = {
            CSixContract.EventEntry.COLUMN_DATE,
            CSixContract.EventEntry.COLUMN_SPEAKER,
            CSixContract.EventEntry.COLUMN_IMAGE,
            CSixContract.EventEntry.COLUMN_TOPIC,
            CSixContract.EventEntry.COLUMN_DESC,
            CSixContract.EventEntry.COLUMN_TYPE
    };

    static final int COL_EVENT_ID        = 0;
    static final int COL_EVENT_DATE      = 1;
    static final int COL_EVENT_SPEAKER   = 2;
    static final int COL_EVENT_IMAGE     = 3;
    static final int COL_EVENT_TOPIC     = 4;
    static final int COL_EVENT_DESC      = 5;
    static final int COL_EVENT_TYPE      = 6;

    public EventWidgetIntentService() {
        super("EventWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, EventWidgetProvider.class));

        Log.i("INFO", "APPWIDGET SIZE: " + appWidgetIds.length);

        Cursor data = getContentResolver().query(
                CSixContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                CSixContract.EventEntry.COLUMN_DATE + " ASC"
        );

        if (data == null) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        String speaker = data.getString(COL_EVENT_SPEAKER);
        Date date = new Date(data.getLong(COL_EVENT_DATE));
        String topic = data.getString(COL_EVENT_TOPIC);

        data.close();

        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_event);
            Log.i("INFO", "SPEAKER " + speaker);
            Log.i("INFO", "DATE " + date);
            Log.i("INFO", "TOPIC " + topic);

            views.setTextViewText(R.id.widget_speaker, speaker);
            views.setTextViewText(R.id.widget_date, DateUtils.getDateString(date));
            views.setTextViewText(R.id.widget_topic, topic);

            // Led to the MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
