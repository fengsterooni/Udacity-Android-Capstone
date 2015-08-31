package org.csix.android.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{

    private final String LOG_TAG = DbHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "csix.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + CSixContract.EventEntry.TABLE_NAME + " (" +
                CSixContract.EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CSixContract.EventEntry.COLUMN_DATE + " INTEGER NOT NULL," +
                CSixContract.EventEntry.COLUMN_SPEAKER + " TEXT NOT NULL," +
                CSixContract.EventEntry.COLUMN_IMAGE + " TEXT NOT NULL," +
                CSixContract.EventEntry.COLUMN_TOPIC + " TEXT NOT NULL," +
                CSixContract.EventEntry.COLUMN_DESC + " TEXT," +
                CSixContract.EventEntry.COLUMN_TYPE + " INTEGER," +

                " UNIQUE (" + CSixContract.EventEntry.COLUMN_DATE + ", " +
                CSixContract.EventEntry.COLUMN_SPEAKER + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_GROUP_TABLE = "CREATE TABLE " + CSixContract.GroupEntry.TABLE_NAME + " (" +
                CSixContract.GroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CSixContract.GroupEntry.COLUMN_NAME + " TEXT NOT NULL," +
                CSixContract.GroupEntry.COLUMN_ADDRESS + " TEXT NOT NULL," +
                CSixContract.GroupEntry.COLUMN_LOCATION + " TEXT NOT NULL," +
                CSixContract.GroupEntry.COLUMN_TIME + " TEXT NOT NULL," +
                CSixContract.GroupEntry.COLUMN_DESC + " TEXT," +

                " UNIQUE (" + CSixContract.GroupEntry.COLUMN_NAME + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_ABOUT_TABLE = "CREATE TABLE " + CSixContract.AboutEntry.TABLE_NAME + " (" +
                CSixContract.AboutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CSixContract.AboutEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                CSixContract.AboutEntry.COLUMN_DESC + " TEXT," +

                " UNIQUE (" + CSixContract.AboutEntry.COLUMN_TITLE + ") ON CONFLICT REPLACE);";

        Log.d(LOG_TAG, SQL_CREATE_GROUP_TABLE);
        db.execSQL(SQL_CREATE_GROUP_TABLE);

        Log.d(LOG_TAG, SQL_CREATE_EVENT_TABLE);
        db.execSQL(SQL_CREATE_EVENT_TABLE);

        Log.d(LOG_TAG, SQL_CREATE_ABOUT_TABLE);
        db.execSQL(SQL_CREATE_ABOUT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

