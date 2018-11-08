package com.example.lallu.meetingscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class AddMeetingActivity extends AppCompatActivity {

    EditText pickdate,starttime,endtime,phonenum,agenda,meetingtitle;
    String amPm,strlocation="aaaaaaaa";
    Calendar calender=Calendar.getInstance();
    private static final int REQUEST_CODE_PICK_CONTACTS=1;
    private Uri uriContact;
    private String contactID,Contactnum,contactName1;
    String stragenda,strdate,strstarttime,strendtime,strtitle,strphn;

    //DataBaseMeeting dbconnect;
    DBHelper dbconnect;
    Button bt;

    //To check field is empty or not
    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                //
                //currentField.setError("invalid");
                Toast.makeText(getApplicationContext(),"Not allowed an empty field",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
//        appBarLayout=(AppBarLayout)findViewById(R.id.appbar);
//        toolbar=(Toolbar)findViewById(R.id.tollbar);
        agenda=(EditText)findViewById(R.id.agenda_edt);
        pickdate=(EditText)findViewById(R.id.meeting_date_edittext);
        starttime=(EditText)findViewById(R.id.start_time_edt);
        endtime=(EditText)findViewById(R.id.End_time_edt);
        phonenum=(EditText)findViewById(R.id.phone_number_edt);
        meetingtitle=(EditText)findViewById(R.id.title_edt);

dbconnect=new DBHelper(this);


        // To choose the meeting Date

        final DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calender.set(Calendar.YEAR,year);
                calender.set(Calendar.MONTH,month);
                calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    upDateLabel();
                }
            }
        };
        pickdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddMeetingActivity.this, date, calender
                        .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                        calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    // To choose start time from Time Picker

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar strttime=Calendar.getInstance();
                int hour=strttime.get(Calendar.HOUR_OF_DAY);
                int mins=strttime.get(Calendar.MINUTE);

                TimePickerDialog mtimePickerDialog=new TimePickerDialog(AddMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                            if (hourOfDay%12==0){
                                hourOfDay=12;
                            }
                            else
                                hourOfDay=hourOfDay%12;
                        } else {
                            amPm = "AM";
                        }

                        starttime.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                    }
                },hour,mins,false);
                mtimePickerDialog.setTitle("Meeting Starts At");
                mtimePickerDialog.show();
            }
        });

        // To Choose End Time

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar strttime=Calendar.getInstance();
                final int hour=strttime.get(Calendar.HOUR_OF_DAY);
                int mins=strttime.get(Calendar.MINUTE);

                TimePickerDialog mtimePickerDialog=new TimePickerDialog(AddMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";

                           // hourOfDay=hourOfDay%12;
                            if (hourOfDay%12==0){
                                hourOfDay=12;
                            }
                            else
                                hourOfDay=hourOfDay%12;

                        }
                       // else
                        else {
                            amPm = "AM";
                        }

                      //  endtime.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                        endtime.setText(String.format("%02d:%02d", hourOfDay, minute) + amPm);
                    }
                },hour,mins,false);
                mtimePickerDialog.setTitle("Meeting Starts At");
                mtimePickerDialog.show();
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void upDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat(myFormat,Locale.US);
        }
        pickdate.setText(sdf.format(calender.getTime()));
    }

    public void SelectFromContacts(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            //   blockListView.setAdapter(dap);
        }
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contactnum=contactNumber.replaceAll("\\s+","");
            if(Contactnum.length()==10){

}
            else if(Contactnum.length()>10){

                phonenum.setText(Contactnum);}
            else{
                Toast.makeText(getApplicationContext(),"No Number Found",Toast.LENGTH_LONG).show();
            }
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {



        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {


            contactName1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+":";

        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName1);
        Log.e("Contactname:",contactName1);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SaveData(View view) {
        Boolean che=validate(new EditText[] {meetingtitle,agenda,pickdate,starttime,endtime,phonenum});
        if (che==true) {
            stragenda = agenda.getText().toString();
            strdate = pickdate.getText().toString();
            strstarttime = starttime.getText().toString();
            strendtime = endtime.getText().toString();
            strphn = phonenum.getText().toString();
            strtitle = meetingtitle.getText().toString();

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String current_Date = df.format(c);
            Toast.makeText(getApplicationContext(), current_Date, Toast.LENGTH_SHORT).show();


            if (dbconnect.insertMeetings(current_Date, strtitle, stragenda, strdate, strstarttime, strendtime, strphn, "dddd")) {

                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }

        }





    }
}
