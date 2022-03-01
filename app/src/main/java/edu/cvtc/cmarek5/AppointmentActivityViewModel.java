package edu.cvtc.cmarek5;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class AppointmentActivityViewModel extends ViewModel {

    // Constants
    public static final String ORIGINAL_APPOINTMENT_FNAME = "edu.cvtc.cmarek5.appointments.ORIGINAL_APPOINTMENT_FIRSTNAME";
    public static final String ORIGINAL_APPOINTMENT_DATE = "edu.cvtc.cmarek5.appointments.ORIGINAL_APPOINTMENT_DATE";

    // Variables
    public String mOriginalAppName;
    public String mOriginalAppDate;
    public boolean mIsNewlyCreated = true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_APPOINTMENT_FNAME, mOriginalAppName);
        outState.putString(ORIGINAL_APPOINTMENT_DATE, mOriginalAppDate);
    }

    public void restoreState(Bundle inState) {
        mOriginalAppName = inState.getString(ORIGINAL_APPOINTMENT_FNAME);
        mOriginalAppDate = inState.getString(ORIGINAL_APPOINTMENT_DATE);
    }

}
