package edu.cvtc.cmarek5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AppointmentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Appointment Activity";

    // DATABASE VARIABLES - CONSTANTS
    public static final String APPOINTMENT_ID = "edu.cvtc.cmarek5.Appointments.APPOINTMENT_ID";
    public static final String ORIGINAL_APPOINTMENT_NAME = "edu.cvtc.cmarek5.appointments.ORIGINAL_APPOINTMENT_NAME";
    public static final String ORIGINAL_APPOINTMENT_DATE = "edu.cvtc.cmarek5.appointments.ORIGINAL_APPOINTMENT_DATE";
    public static final int ID_NOT_SET = -1;

    // Constant to store an ID for the loader
    public static final int LOADER_APPOINTMENTS = 0;

    // Initizliaze new AppointmentInfo to empty
    private AppointmentInfo mAppointment = new AppointmentInfo(0, "", "");

    // Member variables
    private boolean mIsNewAppointment;
    private boolean mIsCancelling;
    private int mAppId;
    private int mAppNamePosition;
    private int mAppDatePosition;
    private String mOriginalAppName;
    private String mOriginalAppDate;

    // Member objects
    private EditText mTextAppName;
    private EditText mTextAppDate;
    private AppointmentOpenHelper mDbOpenHelper;
    private Cursor mAppCursor;

    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate method");
        setContentView(R.layout.activity_appointment);

        // DATABASE CODE
        mDbOpenHelper = new AppointmentOpenHelper(this);

        readDisplayStateValues();

        // Check the saveInstanceState and call appropriate methods
        if (savedInstanceState == null) {
            saveOriginalAppointmentValues();
        } else {
            restoreOriginalAppointmentValues(savedInstanceState);
        }

        // Set reference to the two EditText objects
        mTextAppName = findViewById(R.id.first_name_text);
        mTextAppDate = findViewById(R.id.date_text);

        // If not a new appointment, load the appointment data
        if (!mIsNewAppointment) {
            LoaderManager.getInstance(this).initLoader(LOADER_APPOINTMENTS, null, this);
        }
        // END OF DB CODE
    }

    private void readDisplayStateValues() {

        // Get the intent passed into the activity
        Intent intent = getIntent();

        // Get the appointments id passed into the intent
        mAppId = intent.getIntExtra(APPOINTMENT_ID, ID_NOT_SET);

        // If appointment id is not set, create a new appointment
        mIsNewAppointment = mAppId == ID_NOT_SET;
        if (mIsNewAppointment) {
            createNewAppointment();
        }

    }

    private void loadAppointmentData() {
        // Open a connection to the database
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        // Build the selection criteria. In this case, you want to set the id of the appointment to the
        // passed-in couse id from the intent.
        String selection = AppointmentsDatabaseContract.AppointmentInfoEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(mAppId)};

        // Create a list of the columns you are pulling from the database
        String[] appointmentColumns = {
                AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME,
                AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE
        };

        // Fill your cursor with the information you have provided
        mAppCursor = db.query(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, appointmentColumns, selection, selectionArgs, null, null, null);

        // Get the positions of the fields in the cursor so that you are able to retrieve them in
        //to your layout
        mAppNamePosition = mAppCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME);
        mAppDatePosition = mAppCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE);

        // Make sure that you have moved to the correct record. The cursor will not have populated
        // any of the fields until you move it
        mAppCursor.moveToNext();

        // Call the method to display the appointment
        displayAppointment();
    }

    private void displayAppointment() {
        // Retrieve the values from the cursor based upon the position of the columns
        String appointmentName = mAppCursor.getString(mAppNamePosition);
        String appointmentDate = mAppCursor.getString(mAppDatePosition);

        // USe the information to populate the layout
        mTextAppName.setText(appointmentName);
        mTextAppDate.setText(appointmentDate);
    }


    private void restoreOriginalAppointmentValues(Bundle savedInstanceState) {
        // Get the original values from the savedInstanceState
        mOriginalAppName = savedInstanceState.getString(ORIGINAL_APPOINTMENT_NAME);
        mOriginalAppDate = savedInstanceState.getString(ORIGINAL_APPOINTMENT_DATE);
    }

    private void saveOriginalAppointmentValues() {
        // Only save values if you do not have a new appointment
        if (!mIsNewAppointment) {
            mOriginalAppName = mAppointment.getName();
            mOriginalAppDate = mAppointment.getDate();
        }
    }

    private void storePreviousAppointmentValues() {
        mAppointment.setName(mOriginalAppName);
        mAppointment.setDate(mOriginalAppDate);
    }

    private void saveAppointmentToDatabase(String appointmentName, String appointmentDate) {

        // Create selection criteria
        final String selection = AppointmentsDatabaseContract.AppointmentInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mAppId)};

        // Use a ContentValues object to put our information into
        final ContentValues values = new ContentValues();
        values.put(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME, appointmentName);
        values.put(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE, appointmentDate);

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                // Get connection to the database. Use the writable method since we are changing the
                // data
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                // Call the update method
                db.update(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                return null;
            }
        };

        task.loadInBackground();

    }

    private void createNewAppointment() {

        // Create ContentValues object to hold our fields
        ContentValues values = new ContentValues();

        // For a new appointment, we don't know what the values will be, so we set the columns
        // to empty strings
        values.put(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME, "");
        values.put(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE, "");

        // Get connection to the database. Use the writable method since we are changing the data
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        // Insert the new row in the database and assign the new id to our member variables for
        // appointment id. Cast the 'long' return to value to an int
        mAppId = (int)db.insert(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, null, values);

    }

    // Method for Button
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Component Has Started");
        Button submitButton = findViewById(R.id.button_appointment_submit);

        // This Button Submits Patients Information and Sends it to a Toast
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Log.d(TAG, "Submit Button was Clicked");

                // Variables
                EditText FirstNameText = ((View)view.getParent()).findViewById(R.id.first_name_text);
                EditText LastNameText = ((View)view.getParent()).findViewById(R.id.last_name_text);
                EditText DobText = ((View)view.getParent()).findViewById(R.id.dob_text);
                // Convert to string
                String firstName = FirstNameText.getText().toString();
                String lastName = LastNameText.getText().toString();
                String patientDOB = DobText.getText().toString();

                // Toast to display information
                Toast.makeText(view.getContext(), "Your name is " + firstName + " " + lastName + ". " + "Your DOB is " + patientDOB, Toast.LENGTH_LONG).show();
            }
        });
    }

    // For the Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    // For the Menu
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(AppointmentActivity.this, MainActivity.class);
                intent.putExtra("From Appointment Activity","To Main Activity");
                startActivity(intent);
                return true;
            case R.id.action_staff:
                Intent intentStaff = new Intent(AppointmentActivity.this, StaffActivity.class);
                intentStaff.putExtra("From Appointment Activity", "To Staff Activity");
                startActivity(intentStaff);
                return true;
            case R.id.action_department:
                Intent intentDepartment = new Intent(AppointmentActivity.this, DepartmentActivity.class);
                intentDepartment.putExtra("From Appointment Activity", "To Department Activity");
                startActivity(intentDepartment);
                return true;
            case R.id.action_contact:
                Intent intentContact = new Intent(AppointmentActivity.this, ContactActivity.class);
                intentContact.putExtra("From Appointment Activity", "To Contact Activity");
                startActivity(intentContact);
                return true;
            case R.id.action_database:
                Intent intentDatabase = new Intent(AppointmentActivity.this, DatabaseActivity.class);
                intentDatabase.putExtra("From Staff Activity", "To the Database");
                startActivity(intentDatabase);
                return true;
            default:
                // Do nothing

                // DB CODE
                // Handle action bar item clicks here. The action bar will automatically handle clicks on the
                // Home/Up button, so long as parent activity in AndroidManifest.xml is specified.
                int id = item.getItemId();

                // noinspection simplifiableIfStatement
                if (id == R.id.action_cancel) {
                    mIsCancelling = true;
                    finish();
                }
                // END DB CODE
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPause() {
        super.onPause();
        // Did the user cancel the process?
        if (mIsCancelling) {
            //Is this a new appointment?
            if (mIsNewAppointment) {
                // Delete the new appointment
                deleteAppointmentFromDatabase();
            } else {
                // Put the original values on the screen
                storePreviousAppointmentValues();
            }
        } else {
            // Save the data when leaving the activity
            saveAppointment();
        }
    }

    private void saveAppointment() {
        // Get the values from the layout
        String appointmentName = mTextAppName.getText().toString();
        String appointmentDate = mTextAppDate.getText().toString();

        // Call the method to write to the database
        saveAppointmentToDatabase(appointmentName, appointmentDate);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a local cursor loader
        CursorLoader loader = null;

        // Check to see if the id is for your loader
        if (id == LOADER_APPOINTMENTS) {
            loader = createLoaderAppointments();
        }

        return loader;
    }

    private CursorLoader createLoaderAppointments() {
        return new CursorLoader(this) {

            public Cursor loadInBackgroudn() {
                // Open a connection to the database
                SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                // Build the selection criteria. In this case, you want to set the id of the appointment to the
                // passed-in couse id from the intent.
                String selection = AppointmentsDatabaseContract.AppointmentInfoEntry._ID + " = ?";
                String[] selectionArgs = {Integer.toString(mAppId)};

                // Create a list of the columns you are pulling from the database
                String[] appointmentColumns = {
                        AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME,
                        AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE
                };

                // Fill your cursor with the information you have provided
                return db.query(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, appointmentColumns, selection, selectionArgs, null, null, null);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Check to see if this is your cursor for you loader
        if (loader.getId() == LOADER_APPOINTMENTS) {
            loadFinishedAppointments(data);
        }
    }

    private void loadFinishedAppointments(Cursor data) {
        // Populate your member cursor with the data
        mAppCursor = data;

        // Get the positions of the fields in the cursor so that you are able to retrieve them into
        // your layout
        mAppNamePosition = mAppCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME);
        mAppDatePosition = mAppCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE);

        // Make sure you have moved to the correct record. The cursor will not have populated any of
        // the fields until you move it
        mAppCursor.moveToNext();

        // Call the method to display the appointment
        displayAppointment();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Check to see if this is your cursor from the loader
        if (loader.getId() == LOADER_APPOINTMENTS) {
            // If cursor is not null, close it
            if (mAppCursor != null) {
                mAppCursor.close();
            }
        }
    }

    private void deleteAppointmentFromDatabase() {
        // Create selection criteria
        final String selection = AppointmentsDatabaseContract.AppointmentInfoEntry._ID + " = ?";
        final String[] selectionArgs = {Integer.toString(mAppId)};

        AsyncTaskLoader<String> task = new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                // Get connection to the database. Use the writable method since we are changing the
                // data
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                // Call the delete method
                db.delete(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, selection, selectionArgs);
                return null;
            }
        };

        task.loadInBackground();
    }

}