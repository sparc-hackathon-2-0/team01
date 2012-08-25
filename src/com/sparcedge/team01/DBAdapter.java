package com.sparcedge.team01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * User: bobbystrickland
 * Date: 8/25/12
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "tripppy.db";
    private static final String ITEM_INFO_TABLE = "itemInfo";
    private static final String TRIP_TABLE = "trip";
    private static final int DATABASE_VERSION = 1;

    // The index (key) column name for use in where clauses
    public static final String KEY_ID="_id";

    // The name and column index of each column in your database
    public static final String TRIP_NAME="tripName";
    public static final int TRIP_COLUMN = 1;
    public static final String ITEM_NAME="itemName";
    public static final int ITEM_COLUMN = 2;

    // SQL to create a new db
    private static final String DATABASE_CREATE = "create table " +
            ITEM_INFO_TABLE + " (" + KEY_ID +
            " integer primary key autoincrement, " +
            TRIP_NAME + " string not null, " +
            ITEM_NAME + " string not null);"+
            "create table " +
            TRIP_TABLE + " (" + KEY_ID +
            " integer primary key autoincrement, " +
            TRIP_NAME + " string not null, ";

    // var to hold db instance
    private SQLiteDatabase db;

    // Context of the app using the db
    private final Context context;

    // Db open/upgrade helper
    private myDbHelper dbHelper;

    //@DONE
    public DBAdapter (Context _context) {
        context = _context;
        dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //@DONE
    public DBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        if(db == null) {
            Toast.makeText(context, "DBAdapter.open(): null", Toast.LENGTH_SHORT);
        }
        return this;
    }

    //@DONE
    public void close() {
        db.close();
    }

    //@DONE
    public long addItemToTrip(ItemInfo itemInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRIP_NAME, itemInfo.getTripName());
        contentValues.put(ITEM_NAME, itemInfo.getItemName());
        return db.insert(ITEM_INFO_TABLE, null, contentValues);
    }

    //@DONE
    public long addTrip(String trip) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, trip);
        return db.insert(TRIP_TABLE, null, contentValues);
    }



    //@DONE
    public Boolean removeItemFromTrip(ItemInfo itemInfo) {
        return db.delete(ITEM_INFO_TABLE,
                ITEM_NAME + " = '" + itemInfo.getItemName() + "' AND " + TRIP_NAME + " = '" + itemInfo.getTripName() + "'", null) > 0;
    }


    public ArrayList<String> getTrips() {
        ArrayList<String> lists = new ArrayList<String>();

        Cursor cursor = db.rawQuery("select list from "+TRIP_TABLE, null);

        if((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            cursor.close();
            Tripppy.LOG("Crap. getTrips()");
            return null;
        }
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false) {
            String xlist = cursor.getString(0);
            if(! lists.contains(xlist)) {
                lists.add(xlist);
            }
            cursor.moveToNext();
        }
        return lists;
    }

    //@DONE
    public ArrayList<ItemInfo> getItem(String name) {
        ArrayList<ItemInfo> list = new ArrayList<ItemInfo>();

        Cursor cursor = db.query(true, ITEM_INFO_TABLE,
                new String[] {TRIP_NAME, ITEM_NAME},
                TRIP_NAME + "= '" + name + "'",
                null, null, null, null, null);
        if((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            Tripppy.LOG( "No items found for: " + name);
            cursor.close();
            return null;
        }

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String trip = "";
            try {
                trip = cursor.getString(TRIP_COLUMN);
            } catch (Exception e) {}
            String item = "";
            try {
                item = cursor.getString(ITEM_COLUMN);
            } catch (Exception e) {}

            ItemInfo i = new ItemInfo(trip,item);
            list.add(i);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    //@DONE
    public void wipetable() {
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_INFO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE);
        db.execSQL(DATABASE_CREATE);
    }

//    //@DONE
//    public int getRecordCount() {
//        Cursor cursor = db.rawQuery("select addr from deviceTable", null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count;
//    }

    //@DONE
    private static class myDbHelper extends SQLiteOpenHelper {
        public myDbHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // called when no db exists in disk and helper class
        // needs to create a new one
        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE);
        }

        // called when there is a db version mismatch meaning that
        // the version of the db on disk needs to be upgraded to
        // the current version
        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
                              int _newVersion) {
            // log the version upgrade
            Tripppy.LOG( "Upgrading DB from version " +
                    _oldVersion + " to " +
                    _newVersion +
                    ", which will destroy all old data");

            // upgrade existing db to conform to new version.
            // simple case is to drop old table and create a new one
            _db.execSQL("DROP TABLE IF EXISTS " + ITEM_INFO_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE);
            // craete new one
            onCreate(_db);
        }
    }

}
