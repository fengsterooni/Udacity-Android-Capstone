package org.csix.android.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CSixContract {
    public static final String CONTENT_AUTHORITY = "org.csix";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENT = "event";

    public static final class EventEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENT;

        // Table name
        public static final String TABLE_NAME = "event";

        // Columns
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_SPEAKER = "speaker";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_TOPIC = "topic";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_TYPE = "type";

        public static Uri buildEventUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
