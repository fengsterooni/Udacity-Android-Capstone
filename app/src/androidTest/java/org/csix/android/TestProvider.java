package org.csix.android;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import org.csix.android.data.CSixContract;

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void setUp() {
        deleteAllRecords();
    }

    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                CSixContract.EventEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                CSixContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

     }

    public void testGetType() {

        String type = mContext.getContentResolver().getType(CSixContract.EventEntry.CONTENT_URI);
        assertEquals(CSixContract.EventEntry.CONTENT_TYPE, type);

        long id = 9780137903955L;
        type = mContext.getContentResolver().getType(CSixContract.EventEntry.buildEventUri(id));
        assertEquals(CSixContract.EventEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testInsertRead() {
        ContentValues eventValues = TestDb.getEventValues();

        Uri eventUri = mContext.getContentResolver().insert(CSixContract.EventEntry.CONTENT_URI, eventValues);
        long eventRowId = ContentUris.parseId(eventUri);
        assertTrue(eventRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                CSixContract.EventEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, eventValues);

        cursor = mContext.getContentResolver().query(
                CSixContract.EventEntry.buildEventUri(eventRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, eventValues);

    }
}