package edu.cvtc.cmarek5;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DatabaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Member variables
    private AppointmentOpenHelper mDbOpenHelper;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mAppointmentsLayoutManager;
    private AppointmentRecyclerAdapter mAppointmentRecyclerAdapter;

    public static final int ITEM_APPOINTMENTS = 0;
    public static final int LOADER_APPOINTMENTS = 0;
    // Boolean to check if the 'onCreateLoader' method has run
    private boolean mIsCreated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new AppointmentOpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DatabaseActivity.this, AppointmentActivity.class));
            }
        });

        initializeDisplayContent();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_appointment:
                Intent intent = new Intent(DatabaseActivity.this, AppointmentActivity.class);
                intent.putExtra("From Main Activity","To Appointment Activity");
                startActivity(intent);
                return true;
            case R.id.action_home:
                Intent intentHome = new Intent(DatabaseActivity.this, MainActivity.class);
                intentHome.putExtra("", "");
                startActivity(intentHome);
                return true;
            case R.id.action_staff:
                Intent intentStaff = new Intent(DatabaseActivity.this, StaffActivity.class);
                intentStaff.putExtra("From Main Activity", "To Staff Activity");
                startActivity(intentStaff);
                return true;
            case R.id.action_contact:
                Intent intentContact = new Intent(DatabaseActivity.this, ContactActivity.class);
                intentContact.putExtra("From Main Activity", "To Contact Activity");
                startActivity(intentContact);
                return true;
            case R.id.action_department:
                Intent intentDepartment = new Intent(DatabaseActivity.this, DepartmentActivity.class);
                intentDepartment.putExtra("From Main Activity", "To Department Activity");
                startActivity(intentDepartment);
                return true;
            default:
                // Do nothing

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeDisplayContent() {

        // Retrieve the information from the database
        DataManager.loadFromDatabase(mDbOpenHelper);

        // Set a reference to your list of items layout
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_items);
        mAppointmentsLayoutManager = new LinearLayoutManager(this);

        // Fill the RecyclerAdapter with your appointments
        // (Don't have a cursor yet, so pass null.)
        mAppointmentRecyclerAdapter = new AppointmentRecyclerAdapter(this, null);

        // Display the appointments
        displayAppointments();

    }

    private void displayAppointments() {
        mRecyclerItems.setLayoutManager(mAppointmentsLayoutManager);
        mRecyclerItems.setAdapter(mAppointmentRecyclerAdapter);
    }

    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();

        // Use restartLoader instead of initLoader to make sure to re-quiery the database each time the
        // activity is loaded in the app
        LoaderManager.getInstance(this).restartLoader(LOADER_APPOINTMENTS, null, this);
    }

    private void loadAppointments() {
        // Open your database in read mode
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        // Create a list of colums you want to return
        String[] courseAppointments = {
                AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME,
                AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE,
                AppointmentsDatabaseContract.AppointmentInfoEntry._ID
        };

        // Create an order by field for sorting purposes
        String appointmentOrderBy = AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME;

        // Populate your cursor with the results of the query
        final Cursor courseCursor = db.query(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, courseAppointments, null, null, null, null, appointmentOrderBy);

        // Associate the cursor with your RecyclerAdapter
        mAppointmentRecyclerAdapter.changeCursor(courseCursor);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Create new cursor loader
        CursorLoader loader = null;

        if (id == LOADER_APPOINTMENTS) {
            loader = new CursorLoader(this) {

                @Override
                public Cursor loadInBackground() {
                    mIsCreated = true;

                    // Open your database in read mode
                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

                    // Create a list of columns you want to return
                    String[] appointmentColumns = {
                            AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME,
                            AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE,
                            AppointmentsDatabaseContract.AppointmentInfoEntry._ID
                    };

                    // Create an order by field for sorting purposes
                    String appointmentOrderBy = AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME;

                    // Populate your cursor with the results of the query
                    return db.query(AppointmentsDatabaseContract.AppointmentInfoEntry.TABLE_NAME, appointmentColumns, null, null, null, null, appointmentOrderBy);
                }
            };
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LOADER_APPOINTMENTS && mIsCreated) {
            // Associate the cursor with the RecyclerAdapter
            mAppointmentRecyclerAdapter.changeCursor(data);
            mIsCreated = false;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == LOADER_APPOINTMENTS) {
            // Associate the cursor with your RecyclerAdapter
            mAppointmentRecyclerAdapter.changeCursor(null);
        }
    }
}