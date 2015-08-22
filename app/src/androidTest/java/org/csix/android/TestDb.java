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

    // public final static long ean = 9780137903955L;
    public final static String date = "2015-08-27";
    public final static String speaker = "Jenny Dunham";
    public final static String topic = "Tapping into the Hidden Job Market";
    public final static String desc = "You’ve applied to every job order on line and posted your resume on every job board, but you’re still not getting in the door. What’s next? Many available jobs are unadvertised. Learn how to get in front of hiring managers  without  standing  in  line!  Use  sales  tricks  and  tips  to  warm up  a  cold  call  and  get  past  the gatekeeper to generate informational meetings. Differentiate yourself from the rest of the crowd by doing what other candidates are unwilling to do and afraid to try.\n" +
            "\n" +
            "Jenny Dunham is a Personal Career Coach counseling clients throughout various stages of their career transition.  She is a recognized facilitator and speaker who enjoys uncovering natural talent, pinpointing goals and turning dreams into reality.\n" +
            "\n" +
            "Jenny’s background with sales, recruiting, finance and technology provides a unique perspective with a focus on creating a personal brand to “sell” your background into companies.  Her workshops including “Tapping into the Hidden Job Market”, Laser-Focused Search” and “Building Instant Rapport” encourage clients to step out of their comfort zone to land their ideal job.\n" +
            "\n" +
            "Jenny brings over 15 years of recruiting and business development experience primarily marketing financial consulting services as the Director of Business Development in a boutique staffing firm in the Silicon Valley.  She returned to coaching after several years marketing financial and “big data” software to technology companies as part of a late stage startup.";
    public final static int type = 0;
    
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DbHelper.DATABASE_NAME);
        SQLiteDatabase db = new DbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        DbHelper dbHelper = new DbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = getEventValues();

        long retEan = db.insert(CSixContract.EventEntry.TABLE_NAME, null, values);
        // assertEquals(ean, retEan);

        String[] columns = {
                CSixContract.EventEntry._ID,
                CSixContract.EventEntry.COLUMN_DATE,
                CSixContract.EventEntry.COLUMN_SPEAKER,
                CSixContract.EventEntry.COLUMN_TOPIC,
                CSixContract.EventEntry.COLUMN_DESC,
                CSixContract.EventEntry.COLUMN_TYPE
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                CSixContract.EventEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, values);

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
        // values.put(CSixContract.EventEntry._ID, ean);
        values.put(CSixContract.EventEntry.COLUMN_DATE, date);
        values.put(CSixContract.EventEntry.COLUMN_SPEAKER, speaker);
        values.put(CSixContract.EventEntry.COLUMN_TOPIC, topic);
        values.put(CSixContract.EventEntry.COLUMN_DESC, desc);
        values.put(CSixContract.EventEntry.COLUMN_TYPE, type);

        return values;
    }
}
