package tw.edu.au.csie.simpleschedulebook;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class EventDetail extends AppCompatActivity {

    Button mOk;
    EditText mEvent;
    EditText mDateTime;
    RadioButton mPublic, mPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mOk = (Button)findViewById(R.id.bt_ok);
        mEvent = (EditText)findViewById(R.id.et_event);
        mDateTime = (EditText)findViewById(R.id.et_date);
        mPrivate = (RadioButton)findViewById(R.id.rb_private);
        mPublic = (RadioButton)findViewById(R.id.rb_public);

        Intent intent = getIntent();
        final int action = intent.getIntExtra("ACTION", 0);

        if(action == MainActivity.ACTION_UPDATE){
            String event = intent.getStringExtra("EVENT");
            String datetime = intent.getStringExtra("DATETIME");
            int type = intent.getIntExtra("TYPE", 0);
            mEvent.setText(event);
            mDateTime.setText(datetime);
            if(type == Event.TYPE_PRIVATE) {
                mPrivate.setChecked(true);
            } else {
                mPublic.setChecked(true);
            }
        }

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String event = mEvent.getText().toString();
                String datetime = mDateTime.getText().toString();
                int type = 0;
                if (mPrivate.isChecked()) type = 0;
                else if (mPublic.isChecked()) type = 1;

                SQLiteDatabase db = openOrCreateDatabase("scheduleDb", Context.MODE_PRIVATE, null);

                String sql = "";
                if (action == MainActivity.ACTION_INSERT) {
                    sql = String.format("INSERT INTO schedule (event,datetime,type) VALUES('%s','%s',%d)", event, datetime, type);
                } else {
                    int id = getIntent().getIntExtra("ID", -1);
                    sql = String.format("UPDATE schedule SET event='%s',datetime='%s',type=%d WHERE id=%d", event, datetime, type, id);
                }

                db.execSQL(sql);
                db.close();
                finish();
            }
        });
    }
}