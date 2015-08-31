package org.csix.android;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.csix.android.data.CSixContract;
import org.csix.android.data.DbHelper;

import java.util.Map;
import java.util.Set;

public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Variables for Events
    public final static String date = "2015-08-27";
    public final static String speaker = "Jenny Dunham";
    public final static String topic = "Tapping into the Hidden Job Market";
    public final static String image = "http://csix.org/wp-content/uploads/2015/08/JennyDunham.jpg";
    public final static String event_desc = "You’ve applied to every job order on line and posted your resume on every job board, but you’re still not getting in the door. What’s next? Many available jobs are unadvertised. Learn how to get in front of hiring managers  without  standing  in  line!  Use  sales  tricks  and  tips  to  warm up  a  cold  call  and  get  past  the gatekeeper to generate informational meetings. Differentiate yourself from the rest of the crowd by doing what other candidates are unwilling to do and afraid to try.\n" +
            "\n" +
            "Jenny Dunham is a Personal Career Coach counseling clients throughout various stages of their career transition.  She is a recognized facilitator and speaker who enjoys uncovering natural talent, pinpointing goals and turning dreams into reality.\n" +
            "\n" +
            "Jenny’s background with sales, recruiting, finance and technology provides a unique perspective with a focus on creating a personal brand to “sell” your background into companies.  Her workshops including “Tapping into the Hidden Job Market”, Laser-Focused Search” and “Building Instant Rapport” encourage clients to step out of their comfort zone to land their ideal job.\n" +
            "\n" +
            "Jenny brings over 15 years of recruiting and business development experience primarily marketing financial consulting services as the Director of Business Development in a boutique staffing firm in the Silicon Valley.  She returned to coaching after several years marketing financial and “big data” software to technology companies as part of a late stage startup.";

    public final static int type = 0;


    // Variables for Groups
    public final static String name = "Eco Green Group (EGG)";
    public final static String address = "20390 Park Place, Saratoga, CA";
    public final static String location = "Richards Hall, Saratoga Federated Church";
    public final static String time = "Thursday, 8:00 am – 9:30 am";
    public final static String group_desc = "EcoGreen Group creates professional and entrepreneurial opportunities in sustainability and clean technologies through education, professional development, industry collaboration, and networking.";


    // Variables for Abouts
    public final static String title = "About";
    public final static String about_desc = "CSIX CONNECT helps individuals in career transition to significantly improve their job search success through education, in-person networking and mutual support.   In today’s job market, more than 80 per cent of jobs obtained result from successful networking. CSIX CONNECT provides the means to tap into and leverage the power of a network that is already more than 7000 members strong.";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase db = new DbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testEvent() {
        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Testing Event
        ContentValues values = getEventValues();

        long ret = db.insert(CSixContract.EventEntry.TABLE_NAME, null, values);

        assertEquals(1, ret);

        String[] event_columns = {
                CSixContract.EventEntry._ID,
                CSixContract.EventEntry.COLUMN_DATE,
                CSixContract.EventEntry.COLUMN_SPEAKER,
                CSixContract.EventEntry.COLUMN_IMAGE,
                CSixContract.EventEntry.COLUMN_TOPIC,
                CSixContract.EventEntry.COLUMN_DESC,
                CSixContract.EventEntry.COLUMN_TYPE
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                CSixContract.EventEntry.TABLE_NAME,  // Table to Query
                event_columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, values);

        db.close();
        dbHelper.close();

    }

    public void testGroup() {

        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Testing Group
        ContentValues values = getGroupValues();

        long ret = db.insert(CSixContract.GroupEntry.TABLE_NAME, null, values);
        assertEquals(1, ret);

        String[] group_columns = {
                CSixContract.GroupEntry._ID,
                CSixContract.GroupEntry.COLUMN_NAME,
                CSixContract.GroupEntry.COLUMN_ADDRESS,
                CSixContract.GroupEntry.COLUMN_LOCATION,
                CSixContract.GroupEntry.COLUMN_TIME,
                CSixContract.GroupEntry.COLUMN_DESC
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                CSixContract.GroupEntry.TABLE_NAME,  // Table to Query
                group_columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, values);

        db.close();
        dbHelper.close();
    }

    public void testAbout() {

        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Testing Group
        ContentValues values = getAboutValues();

        long ret = db.insert(CSixContract.AboutEntry.TABLE_NAME, null, values);
        assertEquals(1, ret);

        String[] about_columns = {
                CSixContract.AboutEntry._ID,
                CSixContract.AboutEntry.COLUMN_TITLE,
                CSixContract.AboutEntry.COLUMN_DESC
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                CSixContract.AboutEntry.TABLE_NAME,  // Table to Query
                about_columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, values);

        db.close();
        dbHelper.close();
    }


    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(columnName, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }

    public static ContentValues getEventValues() {

        final ContentValues values = new ContentValues();
        values.put(CSixContract.EventEntry.COLUMN_DATE, date);
        values.put(CSixContract.EventEntry.COLUMN_SPEAKER, speaker);
        values.put(CSixContract.EventEntry.COLUMN_IMAGE, image);
        values.put(CSixContract.EventEntry.COLUMN_TOPIC, topic);
        values.put(CSixContract.EventEntry.COLUMN_DESC, event_desc);
        values.put(CSixContract.EventEntry.COLUMN_TYPE, type);

        return values;
    }

    public static ContentValues getGroupValues() {

        final ContentValues values = new ContentValues();
        values.put(CSixContract.GroupEntry.COLUMN_NAME, name);
        values.put(CSixContract.GroupEntry.COLUMN_ADDRESS, address);
        values.put(CSixContract.GroupEntry.COLUMN_LOCATION, location);
        values.put(CSixContract.GroupEntry.COLUMN_TIME, time);
        values.put(CSixContract.GroupEntry.COLUMN_DESC, group_desc);

        return values;
    }

    public static ContentValues getAboutValues() {

        final ContentValues values = new ContentValues();
        values.put(CSixContract.AboutEntry.COLUMN_TITLE, title);
        values.put(CSixContract.AboutEntry.COLUMN_DESC, about_desc);

        return values;
    }
}
