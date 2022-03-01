package edu.cvtc.cmarek5;

import android.provider.BaseColumns;

public class AppointmentsDatabaseContract {

    private AppointmentsDatabaseContract() {}

        public static final class AppointmentInfoEntry implements BaseColumns {

            public static final String TABLE_NAME = "appointment_info";
            public static final String COLUMN_APPOINTMENT_FNAME = "appointment_firstname";
            public static final String COLUMN_APPOINTMENT_DATE = "appointment_date";

            // Constants to hold values
            public static final String INDEX1 = TABLE_NAME + "_index1";
            public static final String SQL_CREATE_INDEX1 = "CREATE INDEX " + INDEX1 + " ON " +
                    TABLE_NAME + "(" + COLUMN_APPOINTMENT_FNAME + ")";

            // Constant to create the table
            public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " + COLUMN_APPOINTMENT_FNAME + " TEXT NOT NULL, " +
                    COLUMN_APPOINTMENT_DATE + " TEXT)";
        }

}
