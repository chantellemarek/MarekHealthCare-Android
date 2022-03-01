package edu.cvtc.cmarek5;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static DataManager ourInstance = null;
    private List<AppointmentInfo> mAppointments = new ArrayList<>();

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    // Return a list of your appointments
    public List<AppointmentInfo> getAppointments() {
        return mAppointments;
    }

    private static void loadAppointmentsFromDatabase(Cursor cursor) {
        /* Retrieve the field position in the database. The positions of fields may
        change over times as the database grows. Use constants to reference where the
        positions are in the table. */
        int listFirstNamePosition = cursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME);
        int listDatePosition = cursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE);
        int idPosition = cursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry._ID);

        // Create an instance of your DataManager and use the DataManager to
        // clear any information from the array list
        DataManager dm = getInstance();
        dm.mAppointments.clear();

        // Loop through the cursor rowas and add new appointment objects to the array list
        while (cursor.moveToNext()) {
            String listFirstName = cursor.getString(listFirstNamePosition);
            String listDate = cursor.getString(listDatePosition);
            int id = cursor.getInt(idPosition);

            AppointmentInfo list = new AppointmentInfo(id, listFirstName, listDate);

            dm.mAppointments.add(list);
        }

        // Close the cursor (to prevent memory leaks)
        cursor.close();
    }

    public static void loadFromDatabase(AppointmentOpenHelper dbHelper) {
        // Open the database in read mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Create a list of columns
        String[] appointmentColumns = {
                AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME,
                AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE,
                AppointmentsDatabaseContract.AppointmentInfoEntry._ID};

        // Create an order by field for sorting purposes
        String appointmentOrderBy = AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME;

        // Populate your cursor with the results of the query
        final Cursor appointmentCursor = db.query(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, appointmentColumns, null, null, null, null, appointmentOrderBy);

        // Call the method to load your array list
        loadAppointmentsFromDatabase(appointmentCursor);
    }

    public int createNewAppointment() {
        // Create an empty appointment object to use on the activity screen
        // when you want a "blank" record to show up. It will return the size of the new
        // appointments array lst
        AppointmentInfo appointment = new AppointmentInfo(null, null);
        mAppointments.add(appointment);
        return mAppointments.size();
    }

    public void removeAppointment(int index) {
        mAppointments.remove(index);
    }

}
