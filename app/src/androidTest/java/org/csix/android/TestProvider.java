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

    private void deleteAllRecords() {
        deleteAllEvents();
        deleteAllGroups();
        deleteAllAbouts();
    }

    private void deleteAllEvents() {
        // Testing Events
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

    private void deleteAllGroups() {

        // Testing Groups
        mContext.getContentResolver().delete(
                CSixContract.GroupEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                CSixContract.GroupEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(0, cursor.getCount());

        cursor.close();

    }

    private void deleteAllAbouts() {

        // Testing Groups
        mContext.getContentResolver().delete(
                CSixContract.AboutEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                CSixContract.AboutEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals(0, cursor.getCount());

        cursor.close();

    }

    public void testGetTypeEvent() {
        String type = mContext.getContentResolver().getType(CSixContract.EventEntry.CONTENT_URI);
        assertEquals(CSixContract.EventEntry.CONTENT_TYPE, type);

        long id = 9780137903955L;
        type = mContext.getContentResolver().getType(CSixContract.EventEntry.buildEventUri(id));
        assertEquals(CSixContract.EventEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testGetTypeGroup() {
        String type = mContext.getContentResolver().getType(CSixContract.GroupEntry.CONTENT_URI);
        assertEquals(CSixContract.GroupEntry.CONTENT_TYPE, type);

        long id = 9780137903955L;
        type = mContext.getContentResolver().getType(CSixContract.GroupEntry.buildGroupUri(id));
        assertEquals(CSixContract.GroupEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testGetTypeAbout() {
        String type = mContext.getContentResolver().getType(CSixContract.AboutEntry.CONTENT_URI);
        assertEquals(CSixContract.AboutEntry.CONTENT_TYPE, type);

        long id = 9780137903955L;
        type = mContext.getContentResolver().getType(CSixContract.AboutEntry.buildAboutUri(id));
        assertEquals(CSixContract.AboutEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testInsertRead() {
        testInsertReadEvents();
        testInsertReadGroups();
        testInsertReadAbouts();
    }

    public void testInsertReadEvents() {
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

        cursor.close();
    }

    public void testInsertReadGroups() {

        ContentValues groupValues = TestDb.getGroupValues();

        Uri groupUri = mContext.getContentResolver().insert(CSixContract.GroupEntry.CONTENT_URI, groupValues);
        long groupRowId = ContentUris.parseId(groupUri);
        assertTrue(groupRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                CSixContract.GroupEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, groupValues);

        cursor = mContext.getContentResolver().query(
                CSixContract.GroupEntry.buildGroupUri(groupRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, groupValues);

        cursor.close();
    }

    public void testInsertReadAbouts() {

        ContentValues aboutValues = TestDb.getAboutValues();

        Uri aboutUri = mContext.getContentResolver().insert(CSixContract.AboutEntry.CONTENT_URI, aboutValues);
        long aboutRowId = ContentUris.parseId(aboutUri);
        assertTrue(aboutRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                CSixContract.AboutEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, aboutValues);

        cursor = mContext.getContentResolver().query(
                CSixContract.AboutEntry.buildAboutUri(aboutRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, aboutValues);

        cursor.close();
    }
}
