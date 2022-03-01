package edu.cvtc.cmarek5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "Contact Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
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
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                intent.putExtra("From Contact Activity 2","To Main Activity");
                startActivity(intent);
                return true;
            case R.id.action_staff:
                Intent intentStaff = new Intent(ContactActivity.this, StaffActivity.class);
                intentStaff.putExtra("From Contact Activity", "To Staff Activity");
                startActivity(intentStaff);
                return true;
            case R.id.action_appointment:
                Intent intentAppointment = new Intent(ContactActivity.this, AppointmentActivity.class);
                intentAppointment.putExtra("From Contact Activity", "To Appointment Activity");
                startActivity(intentAppointment);
                return true;
            case R.id.action_department:
                Intent intentDepartment = new Intent(ContactActivity.this, DepartmentActivity.class);
                intentDepartment.putExtra("From Contact Activity", "To Department Activity");
                startActivity(intentDepartment);
                return true;
            case R.id.action_database:
                Intent intentDatabase = new Intent(ContactActivity.this, DatabaseActivity.class);
                intentDatabase.putExtra("From Staff Activity", "To the Database");
                startActivity(intentDatabase);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    // Method for Button
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart method for button");

        Button submitButton = findViewById(R.id.button_contact_submit);

        // This Button Submits Patients Information and Sends it to a Toast
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Log.d(TAG, "onClick Method for the Contact Button");

                // Variables
                EditText firstNameText = ((View)view.getParent()).findViewById(R.id.contact_first_name_text);
                EditText lastNameText = ((View)view.getParent()).findViewById(R.id.contact_last_name_text);
                EditText patientText = ((View)view.getParent()).findViewById(R.id.patient_id_text);
                EditText emailText = ((View)view.getParent()).findViewById(R.id.email_text);
                EditText phoneText = ((View)view.getParent()).findViewById(R.id.phone_number_text);
                EditText messageText = ((View)view.getParent()).findViewById(R.id.message_text);

                // Convert to string
                String firstName = firstNameText.getText().toString();
                String lastName = lastNameText.getText().toString();
                String patientId = patientText.getText().toString();
                String email = emailText.getText().toString();
                String phone = phoneText.getText().toString();
                String message = messageText.getText().toString();

                // Send User to Recieved Message Activity
                startActivity(new Intent(ContactActivity.this, RecievedActivity.class));

                // Toast to display information
                Toast.makeText(view.getContext(), "Name: " + firstName + " " + lastName + "\n"
                        + "Patient ID: " + patientId + "\n" +
                        "Contact Info: " + email + " " + phone + "\n" +
                        "Message: " + message, Toast.LENGTH_LONG).show();

            }
        });
    }
}