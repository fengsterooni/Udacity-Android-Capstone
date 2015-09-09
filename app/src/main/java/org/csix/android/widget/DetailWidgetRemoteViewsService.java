package org.csix.android.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.csix.android.DateUtils;
import org.csix.android.EventDetailActivity;
import org.csix.android.R;
import org.csix.android.data.CSixContract;

import java.util.Date;

public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();
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

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsService.RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(
                        CSixContract.EventEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        CSixContract.EventEntry.COLUMN_DATE + " ASC"
                );

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);

                long id = data.getLong(COL_EVENT_ID);
                String speaker = data.getString(COL_EVENT_SPEAKER);
                Date date = new Date(data.getLong(COL_EVENT_DATE));
                String topic = data.getString(COL_EVENT_TOPIC);

                views.setTextViewText(R.id.widget_speaker, speaker);
                views.setTextViewText(R.id.widget_month, DateUtils.getShortMonthString(date));
                views.setTextViewText(R.id.widget_day, DateUtils.getDayString(date));
                views.setTextViewText(R.id.widget_topic, topic);

                final Intent fillInIntent = new Intent();

                Uri eventUri = CSixContract.EventEntry.CONTENT_URI;

                fillInIntent.setData(eventUri);
                fillInIntent.putExtra(EventDetailActivity.EVENT_ID, id);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                // views.setContentDescription(R.id.widget_icon, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(1);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
