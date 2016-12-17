package tw.edu.au.csie.simpleschedulebook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static int ACTION_INSERT = 0;
    final static int ACTION_UPDATE = 1;

    String dbPath;

    /* List data in database */
    ListView mListView;

    SQLiteDatabase db;

    ArrayList<Event> mItemList;
    EventAdapter mAdapter;

    private void onDbUpdate() {

        mItemList = new ArrayList<Event>();

        Cursor c = db.rawQuery("SELECT * FROM schedule", null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            mItemList.add(new Event(
                    c.getInt(0),     // id
                    c.getString(1),  // event
                    c.getString(2),  // datetime
                    c.getInt(3))     // type
            );
            c.moveToNext();
        }

        mAdapter = new EventAdapter(this, R.layout.item, mItemList);
        mListView.setAdapter(mAdapter);
    }

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = getApplicationContext().getAssets().open("scheduleDb");

        // Path to the just created empty db
        String dbPath = getApplicationContext().getDatabasePath("").getPath();
        Toast.makeText(this,dbPath,Toast.LENGTH_LONG).show();

        File dbDir = new File(dbPath);
        if(!dbDir.exists())
            dbDir.mkdir();

        dbPath = getApplicationContext().getDatabasePath("scheduleDb").getPath();
        File outFileName = getDatabasePath(dbPath);

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbPath = getApplicationContext().getDatabasePath("scheduleDb").getPath();

        mListView = (ListView)findViewById(R.id.lv);
        registerForContextMenu(mListView);

        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch(SQLiteException e) {
            try {
                copyDataBase();
                db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            } catch (IOException e1) {
                Toast.makeText(this, "Copy Database Failed", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onDbUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null)
            db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_event:
                //Toast.makeText(this,"ADD",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EventDetail.class);
                intent.putExtra("ACTION", ACTION_INSERT);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = mItemList.get(info.position).getId();
        int type = mItemList.get(info.position).getType();
        String event = mItemList.get(info.position).getEvent();
        String datetime = mItemList.get(info.position).getDatetime();

        switch(item.getItemId())
        {
            case R.id.update:
                //Toast.makeText(this,"UPDATE",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EventDetail.class);
                intent.putExtra("ACTION", ACTION_UPDATE);
                intent.putExtra("ID", id);
                intent.putExtra("TYPE", type);
                intent.putExtra("EVENT",event);
                intent.putExtra("DATETIME",datetime);
                startActivity(intent);
                break;
            case R.id.delete:
                //Toast.makeText(this,"DELETE",Toast.LENGTH_SHORT).show();
                String sql = String.format("DELETE FROM schedule WHERE id=%d", id);
                db.execSQL(sql);
                onDbUpdate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}