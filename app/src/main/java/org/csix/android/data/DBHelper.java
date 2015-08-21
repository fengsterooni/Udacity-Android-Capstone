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
                CSixContract.EventEntry.COLUMN_TOPIC + " TEXT NOT NULL," +
                CSixContract.EventEntry.COLUMN_DESC + " TEXT," +
                CSixContract.EventEntry.COLUMN_TYPE + " INTEGER," +

                " UNIQUE (" + CSixContract.EventEntry.COLUMN_DATE + ", " +
                CSixContract.EventEntry.COLUMN_SPEAKER + ") ON CONFLICT REPLACE);";

        Log.d(LOG_TAG, SQL_CREATE_EVENT_TABLE);

        db.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

