package edu.cvtc.cmarek5;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class AppointmentDataWorker {

    // Member Attributes
    private SQLiteDatabase mDb;

    // Constructor
    public AppointmentDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    private void insertAppointment(String firstName, String date) {
        ContentValues values = new ContentValues();
        values.put(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME, firstName);
        values.put(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE, date);

        long newRowId = mDb.insert(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, null, values);
    }

    public void insertAppointment() {

        insertAppointment("Chantelle", "03/18/2021");
        insertAppointment("Homer", "08/03/2021");
        insertAppointment("Marge", "03/18/2021");
        insertAppointment("Bart", "05/11/2021");
        insertAppointment("Lisa", "09/20/2021");
        insertAppointment("Maggie", "07/25/2021");
    }

}
