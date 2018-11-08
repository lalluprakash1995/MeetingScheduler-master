package com.example.lallu.meetingscheduler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllMeetings extends AppCompatActivity {
    ArrayList<String> meetinghead;
    ArrayList<String> meetingdate;
    ListView list;
    DBHelper mydbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_meetings);
        mydbhelper=new DBHelper(this);
        meetinghead=mydbhelper.getAllCotacts();
        meetingdate=mydbhelper.getAllDates();
//        meetinghead.add("aaaaaa");
//        meetinghead.add("bbbbbb");
//        meetinghead.add("ccccccc");
//        meetingdate.add("12/04/2018");
//        meetingdate.add("34/67/2018");
//        meetingdate.add("78/09/1234");
        list=(ListView)findViewById(R.id.hosplist);
        adapter ad=new adapter();
        list.setAdapter(ad);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itm=((TextView)view.findViewById(R.id.txtview1)).getText().toString();

                Toast.makeText(getApplicationContext(), itm,Toast.LENGTH_SHORT).show();
                Bundle dataBundle = new Bundle();
                //   dataBundle.putInt("id", id_To_Search);
                dataBundle.putString("title",itm);

                Intent intent = new Intent(getApplicationContext(),ViewMeetings.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
                // View v1 = adapterView.getChildAt(position);

            }
        });

    }

    public void addnew(View view) {
        startActivity(new Intent(getApplicationContext(),AddMeetingActivity.class));
    }

    class adapter extends BaseAdapter{
        LayoutInflater inflater;

        @Override
        public int getCount() {
            return meetingdate.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.conect,null);
            ViewHolder holder=new ViewHolder();
            holder.Hna=(TextView)view.findViewById(R.id.txtview1);
            holder.Hna.setText(meetinghead.get(i));
            holder.Pla=(TextView)view.findViewById(R.id.txtview2);
            holder.Pla.setText(meetingdate.get(i));
            return view;
        }
    }
    class ViewHolder{
        TextView Hna;
        TextView Pla;



    }
}
