package com.ryanzhou.company.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ryanzhou.company.habittracker.HabitTrackerContract.HabitEntry;

public class MainActivity extends AppCompatActivity {

    public final String LOG_TAG = getClass().getSimpleName();
    HabitDbHelper habitDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voidTestHabitDb();
    }

    private void voidTestHabitDb() {

        Log.d(LOG_TAG, "Creating table");
        habitDbHelper = new HabitDbHelper(getApplicationContext());

        final String habit1name = "JOGGING";
        final int habit1freq = 1;
        Log.d(LOG_TAG, "Inserting item = " + habit1name + ": " + habit1freq);
        insertHabitWith(habit1name, habit1freq);
        printHabitTable();

        final String habit2name = "READING";
        final int habit2freq = 2;
        Log.d(LOG_TAG, "Inserting item = " + habit2name + ": " + habit2freq);
        insertHabitWith(habit2name, habit2freq);
        printHabitTable();

        final int habit1newfreq = 3;
        Log.d(LOG_TAG, "Updating item = " + habit1name + " old freq: " + habit1freq + " with new freq: " + habit1newfreq);
        updateHabitWithNewFrequency(habit1name, habit1newfreq);
        printHabitTable();

        Log.d(LOG_TAG, "Getting item = " + habit1name + ": " + habit1newfreq);
        printRowWithName(habit1name);
    }


    private void insertHabitWith(String name, int frequency) {
        SQLiteDatabase sqLiteDatabase = habitDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_NAME_TITLE, name);
        values.put(HabitEntry.COLUMN_NAME_FREQUENCY, frequency);
        long newRowId = sqLiteDatabase.insert(HabitEntry.TABLE_NAME, null, values);
    }

    private void printHabitTable() {
        SQLiteDatabase sqLiteDatabase = habitDbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + HabitEntry.TABLE_NAME, null);
        Log.d(LOG_TAG, "Printing habit table");
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(cursor.getColumnIndex(HabitEntry._ID));
                String nameTitle = cursor.getString(cursor.getColumnIndex(HabitEntry.COLUMN_NAME_TITLE));
                int frequency = cursor.getInt(cursor.getColumnIndex(HabitEntry.COLUMN_NAME_FREQUENCY));
                Log.d(LOG_TAG, "ID: " + id + "\t name: " + nameTitle + "\t freq: " + frequency);
                cursor.moveToNext();
            }
            Log.d(LOG_TAG, "\n");
        } else {
            Log.d(LOG_TAG, "Nothing to print");
        }
        cursor.close();
    }

    private void printRowWithName(String habitName) {
        SQLiteDatabase sqLiteDatabase = habitDbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT *" +
                " FROM " + HabitEntry.TABLE_NAME +
                " WHERE "+ HabitEntry.COLUMN_NAME_TITLE + " = " + "\'" + habitName + "\'", null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(HabitEntry._ID));
            String nameTitle = cursor.getString(cursor.getColumnIndex(HabitEntry.COLUMN_NAME_TITLE));
            int frequency = cursor.getInt(cursor.getColumnIndex(HabitEntry.COLUMN_NAME_FREQUENCY));
            Log.d(LOG_TAG, "ID: " + id + "\t name: " + nameTitle + "\t freq: " + frequency);
        } else {
            Log.d(LOG_TAG, "No habit with name: " + habitName + " found");
        }
        cursor.close();
    }

    private void updateHabitWithNewFrequency(String habitToUpdate, int newFrequency) {
        SQLiteDatabase sqLiteDatabase = habitDbHelper.getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_NAME_TITLE, habitToUpdate);
        values.put(HabitEntry.COLUMN_NAME_FREQUENCY, newFrequency);

        // Which row to update, based on the ID
        String selection = HabitEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {habitToUpdate};

        int count = sqLiteDatabase.update(
                HabitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }


    private void deleteAllEntries() {
        SQLiteDatabase sqLiteDatabase = habitDbHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(habitDbHelper.getSqlDeleteEntries());
    }

}
