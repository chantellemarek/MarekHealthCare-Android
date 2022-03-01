package edu.cvtc.cmarek5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.action_appointment:
                Intent intent = new Intent(MainActivity.this, AppointmentActivity.class);
                intent.putExtra("From Main Activity","To Appointment Activity");
                startActivity(intent);
                return true;
            case R.id.action_staff:
                Intent intentStaff = new Intent(MainActivity.this, StaffActivity.class);
                intentStaff.putExtra("From Main Activity", "To Staff Activity");
                startActivity(intentStaff);
                return true;
            case R.id.action_contact:
                Intent intentContact = new Intent(MainActivity.this, ContactActivity.class);
                intentContact.putExtra("From Main Activity", "To Contact Activity");
                startActivity(intentContact);
                return true;
            case R.id.action_department:
                Intent intentDepartment = new Intent(MainActivity.this, DepartmentActivity.class);
                intentDepartment.putExtra("From Main Activity", "To Department Activity");
                startActivity(intentDepartment);
                return true;
            case R.id.action_database:
                Intent intentDatabase = new Intent(MainActivity.this, DatabaseActivity.class);
                intentDatabase.putExtra("From Main", "To Database");
                startActivity(intentDatabase);
                return true;
            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }

}