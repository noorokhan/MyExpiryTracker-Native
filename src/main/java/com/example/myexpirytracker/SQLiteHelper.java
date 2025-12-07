package com.example.myexpirytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expiry_tracker.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "expiry_items";

    private static final String COL_ID = "id";
    private static final String COL_CATEGORY = "category";
    private static final String COL_ITEM_NAME = "item_name";
    private static final String COL_DATE = "expiry_date";
    private static final String COL_CYCLE = "cycle";
    private static final String COL_PRICE = "price";
    private static final String COL_NOTES = "notes";
    private static final String COL_REMINDER = "reminder_days";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY + " TEXT, " +
                COL_ITEM_NAME + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_CYCLE + " TEXT, " +
                COL_PRICE + " TEXT, " +
                COL_NOTES + " TEXT, " +
                COL_REMINDER + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new item
    public long insertExpiry(String category, String itemName, String date, String cycle, String price, String notes, String reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_CATEGORY, category);
        cv.put(COL_ITEM_NAME, itemName);
        cv.put(COL_DATE, date);
        cv.put(COL_CYCLE, cycle);
        cv.put(COL_PRICE, price);
        cv.put(COL_NOTES, notes);
        cv.put(COL_REMINDER, reminder);

        return db.insert(TABLE_NAME, null, cv);
    }

    // Count total items stored
    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Get all items to display in View All screen
    public List<String> getAllItems() {
        List<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_NAME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            String cycle = cursor.getString(cursor.getColumnIndexOrThrow(COL_CYCLE));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES));
            String reminder = cursor.getString(cursor.getColumnIndexOrThrow(COL_REMINDER));

            String item = "Category: " + category +
                    "\nName: " + name +
                    "\nExpiry Date: " + date +
                    "\nCycle: " + cycle +
                    "\nPrice: " + price +
                    "\nNotes: " + notes +
                    "\nReminder: " + reminder;

            list.add(item);
        }


        cursor.close();
        return list;

    }
    // Delete all items
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

}
