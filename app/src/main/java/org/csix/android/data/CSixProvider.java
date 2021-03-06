package org.csix.android.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CSixProvider extends ContentProvider {

    private final String LOG_TAG = CSixProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int EVENT_ID = 100;
    private static final int EVENT = 101;
    private static final int GROUP_ID = 200;
    private static final int GROUP = 201;
    private static final int ABOUT_ID = 300;
    private static final int ABOUT = 301;

    private DbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CSixContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, CSixContract.PATH_EVENT + "/#", EVENT_ID);
        matcher.addURI(authority, CSixContract.PATH_EVENT, EVENT);

        matcher.addURI(authority, CSixContract.PATH_GROUP + "/#", GROUP_ID);
        matcher.addURI(authority, CSixContract.PATH_GROUP, GROUP);

        matcher.addURI(authority, CSixContract.PATH_ABOUT + "/#", ABOUT_ID);
        matcher.addURI(authority, CSixContract.PATH_ABOUT, ABOUT);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case EVENT:
                retCursor = dbHelper.getReadableDatabase().query(
                        CSixContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case EVENT_ID:
                retCursor = dbHelper.getReadableDatabase().query(
                        CSixContract.EventEntry.TABLE_NAME,
                        projection,
                        CSixContract.EventEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case GROUP:
                retCursor = dbHelper.getReadableDatabase().query(
                        CSixContract.GroupEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case GROUP_ID:
                retCursor = dbHelper.getReadableDatabase().query(
                        CSixContract.GroupEntry.TABLE_NAME,
                        projection,
                        CSixContract.GroupEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ABOUT:
                retCursor = dbHelper.getReadableDatabase().query(
                        CSixContract.AboutEntry.TABLE_NAME,
                        projection,
                        selection,
                        selection == null ? null : selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ABOUT_ID:
                retCursor = dbHelper.getReadableDatabase().query(
                        CSixContract.AboutEntry.TABLE_NAME,
                        projection,
                        CSixContract.AboutEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case EVENT_ID:
                return CSixContract.EventEntry.CONTENT_ITEM_TYPE;
            case EVENT:
                return CSixContract.EventEntry.CONTENT_TYPE;
            case GROUP_ID:
                return CSixContract.GroupEntry.CONTENT_ITEM_TYPE;
            case GROUP:
                return CSixContract.GroupEntry.CONTENT_TYPE;
            case ABOUT_ID:
                return CSixContract.AboutEntry.CONTENT_ITEM_TYPE;
            case ABOUT:
                return CSixContract.AboutEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case EVENT: {
                long _id = db.insert(CSixContract.EventEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CSixContract.EventEntry.buildEventUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case GROUP: {
                long _id = db.insert(CSixContract.GroupEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CSixContract.GroupEntry.buildGroupUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case ABOUT: {
                long _id = db.insert(CSixContract.AboutEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CSixContract.AboutEntry.buildAboutUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case EVENT:
                rowsDeleted = db.delete(
                        CSixContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EVENT_ID:
                rowsDeleted = db.delete(
                        CSixContract.EventEntry.TABLE_NAME,
                        CSixContract.EventEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            case GROUP:
                rowsDeleted = db.delete(
                        CSixContract.GroupEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GROUP_ID:
                rowsDeleted = db.delete(
                        CSixContract.GroupEntry.TABLE_NAME,
                        CSixContract.GroupEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            case ABOUT:
                rowsDeleted = db.delete(
                        CSixContract.AboutEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ABOUT_ID:
                rowsDeleted = db.delete(
                        CSixContract.AboutEntry.TABLE_NAME,
                        CSixContract.AboutEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case EVENT:
                rowsUpdated = db.update(CSixContract.EventEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case GROUP:
                rowsUpdated = db.update(CSixContract.GroupEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ABOUT:
                rowsUpdated = db.update(CSixContract.AboutEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case EVENT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CSixContract.EventEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case GROUP:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CSixContract.GroupEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case ABOUT:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(CSixContract.AboutEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
