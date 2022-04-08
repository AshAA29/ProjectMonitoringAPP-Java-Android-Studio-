package com.example.projectmonitoing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MakeAgenda extends AppCompatActivity {
    EditText _textBox;
    Button _btnSubmitAgenda;
    ListView _AttList;
    SQLiteOpenHelper openHelper2;
    SQLiteDatabase db;
    Cursor cursor,userCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_agenda);
        //getWindow().getDecorView().setBackgroundColor(Color.#);
        openHelper2 = new DatabaseHelper(this);
        _textBox = (EditText) findViewById(R.id.agendaText);
        _btnSubmitAgenda = (Button) findViewById(R.id.btnSubmitAgenda);
        _AttList = (ListView) findViewById(R.id.attListBox);

        DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);
        final ArrayList<String> attendanceList = myDatabaseHelper.getGroupMemberArray(login.getGroupNumber());
        ArrayAdapter attAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,attendanceList);
        _AttList.setAdapter(attAdapt);
        Boolean myStat = false;
        //Toast.makeText(getApplicationContext(), "Item: " + attendanceList.get(1), Toast.LENGTH_LONG).show();
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.UK.getDefault()).format(new Date());

        _AttList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db = openHelper2.getWritableDatabase();
                view.setBackgroundColor(Color.rgb(170,255,128));
                String[] parts = attendanceList.get(position).split(" ");
                cursor = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME3 + " WHERE " + DatabaseHelper.COL_112 + " =? AND " + DatabaseHelper.COL_1122 + " =?" + " AND " + DatabaseHelper.COL_113 + " =? " + " AND "+ DatabaseHelper.COL_114 + "=?", new String[]{parts[0],parts[1],"Attended",date});
                if(cursor != null)
                {
                    if(cursor.getCount()>0)
                    {
                        cursor.moveToNext();
                        Toast.makeText(MakeAgenda.this, "Marked In Already: " + attendanceList.get(position), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MakeAgenda.this, "Marked In: " + attendanceList.get(position), Toast.LENGTH_SHORT).show();

                        insertData3(parts[0],parts[1],"Attended",date,login.getGroupNumber());
                    }
                }
            }
        });

        _btnSubmitAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper2.getWritableDatabase();
                String AgendaText = _textBox.getText().toString();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.UK.getDefault()).format(new Date());
                insertData2(AgendaText,date,login.getGroupNumber());
                Toast.makeText(getApplicationContext(), "You submitted the agenda successfully", Toast.LENGTH_SHORT).show();

                for(int i=0;i<attendanceList.size();i++)
                {
                    String[] parts = attendanceList.get(i).split(" ");
                    cursor = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME3 + " WHERE "+ DatabaseHelper.COL_112 + " =?" + " AND " + DatabaseHelper.COL_1122 + " =?" + " AND "+ DatabaseHelper.COL_113 + " =? " + " AND " + DatabaseHelper.COL_114 + "=?", new String[]{parts[0],parts[1],"Attended",date});
                    if(cursor != null)
                    {
                        if(cursor.getCount()>0)
                        {
                            cursor.moveToNext();
                        }
                        else
                        {
                            cursor = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME3 + " WHERE "+ DatabaseHelper.COL_112 + " =?" + " AND " + DatabaseHelper.COL_1122 + " =? " + " AND "+ DatabaseHelper.COL_113 + " =? " + " AND " + DatabaseHelper.COL_114 + "=?", new String[]{parts[0],parts[1],"Not Present",date});
                            if(cursor != null)
                            {
                                if (cursor.getCount() > 0)
                                {
                                    cursor.moveToNext();
                                }
                                else
                                {
                                    insertData3(parts[0],parts[1],"Not Present",date,login.getGroupNumber());
                                }
                            }
                        }
                    }
                }
                Intent intent = new Intent(MakeAgenda.this, Home.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });


    }


    public void insertData2(String agenda, String date, String groupNO)
    {
        ContentValues contentValues2 = new ContentValues();
        //contentValues2.put(DatabaseHelper.COL_21, username);
        contentValues2.put(DatabaseHelper.COL_31, agenda);
        contentValues2.put(DatabaseHelper.COL_41, date);
        contentValues2.put(DatabaseHelper.COL_51, groupNO);
        long id = db.insert(DatabaseHelper.TABLE_NAME2, null, contentValues2);


    }

    public void insertData3(String FirstName,String LastName, String Status, String date, String groupNO)
    {
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(DatabaseHelper.COL_112, FirstName);
        contentValues3.put(DatabaseHelper.COL_1122, LastName);
        contentValues3.put(DatabaseHelper.COL_113, Status);
        contentValues3.put(DatabaseHelper.COL_114, date);
        contentValues3.put(DatabaseHelper.COL_115, groupNO);
        long id = db.insert(DatabaseHelper.TABLE_NAME3, null, contentValues3);


    }
}
