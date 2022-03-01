package edu.cvtc.cmarek5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppointmentOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Appointments_cmarek5.db";
    public static final int DATABASE_VERSION = 1;

    public AppointmentOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(AppointmentsDatabaseContract.AppointmentInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(AppointmentsDatabaseContract.AppointmentInfoEntry.SQL_CREATE_INDEX1);

        AppointmentDataWorker worker = new AppointmentDataWorker(db);
        worker.insertAppointment();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
