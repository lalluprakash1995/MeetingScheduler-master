package com.example.lallu.meetingscheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MeetingInWeek extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_in_week);
        DBHelper dbh=new DBHelper(this);
        ListView list=(ListView)findViewById(R.id.meetinglist);
        ArrayAdapter adapt;
        ArrayList arrayList=dbh.getAllMeetings();

        adapt=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);








        list.setAdapter(adapt);
    }
}
