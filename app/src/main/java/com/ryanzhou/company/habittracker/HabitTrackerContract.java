package com.ryanzhou.company.habittracker;

import android.provider.BaseColumns;

/**
 * Created by ryanzhou on 6/26/16.
 */
public class HabitTrackerContract {

    public HabitTrackerContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "habitTable";
        public static final String COLUMN_NAME_TITLE = "titleColumn";
        public static final String COLUMN_NAME_FREQUENCY = "freqColumn";
    }
}
