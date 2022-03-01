package edu.cvtc.cmarek5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class StaffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
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
                Intent intentHome = new Intent(StaffActivity.this, MainActivity.class);
                intentHome.putExtra("From Staff Activity", "To Main Activity");
                startActivity(intentHome);
                return true;
            case R.id.action_appointment:
                Intent intentAppointment = new Intent(StaffActivity.this, AppointmentActivity.class);
                intentAppointment.putExtra("From Staff Activity","To Appointment Activity");
                startActivity(intentAppointment);
                return true;
            case R.id.action_department:
                Intent intentDepartment = new Intent(StaffActivity.this, DepartmentActivity.class);
                intentDepartment.putExtra("From Staff Activity", "To Department Activity");
                startActivity(intentDepartment);
                return true;
            case R.id.action_contact:
                Intent intentContact = new Intent(StaffActivity.this, ContactActivity.class);
                intentContact.putExtra("From Staff Activity", "To Contact Activity");
                startActivity(intentContact);
                return true;
            case R.id.action_database:
                Intent intentDatabase = new Intent(StaffActivity.this, DatabaseActivity.class);
                intentDatabase.putExtra("From Staff Activity", "To the Database");
                startActivity(intentDatabase);
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onClick(View view) {
        Intent intent = new Intent(StaffActivity.this, StaffDescriptionActivity.class);
        intent.putExtra("From Staff Activity", "To Staff Description");
        startActivity(intent);
        return true;
    }
}