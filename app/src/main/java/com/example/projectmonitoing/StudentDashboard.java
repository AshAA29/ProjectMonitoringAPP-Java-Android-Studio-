package com.example.projectmonitoing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentDashboard extends AppCompatActivity {
    Button _btnStudentDashSearch,_btnStudentDashRemoveGroup;
    EditText _StudentDashSearch;
    SQLiteDatabase db;
    Cursor cursor;
    SQLiteOpenHelper openHelper;
    ListView _studentListView;

    public static String getFullName() {
        return FullName;
    }
    public static String FullName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        _btnStudentDashSearch = (Button) findViewById(R.id.btnStudentDashSearch);
        _btnStudentDashRemoveGroup = (Button) findViewById(R.id.btnStudentDashRemoveGroup);
        _StudentDashSearch = (EditText) findViewById(R.id.StudentDashSearch);
        _studentListView = (ListView) findViewById(R.id.studentListView);
        DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);
        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();

        final ArrayList<String> studentList = myDatabaseHelper.getFullStudentArray();
        ArrayAdapter studentAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,studentList);
        _studentListView.setAdapter(studentAdapt);

        _btnStudentDashSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullName = _StudentDashSearch.getText().toString();
                String[] parts = _StudentDashSearch.getText().toString().split(" ");
                String firstName = " ";
                String lastName = " ";
                if (parts.length >= 2)
                {
                    firstName = parts[0];
                    lastName = parts[1];
                }
                else {
                    firstName = " ";
                    lastName = " ";
                }
                cursor = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_2 + " =? " + " AND " + DatabaseHelper.COL_3 + " =? ", new String[]{firstName,lastName});
                if(cursor != null)
                {
                    if(cursor.getCount()>0)
                    {
                        cursor.moveToNext();
                         //Toast.makeText(getApplicationContext(), "User found",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StudentDashboard.this, StudentUpdate.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        _studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] parts = studentList.get(position).split(" ");
                String firstName = parts[0];
                String lastName = parts[1];
                _StudentDashSearch.setText(firstName + " " + lastName);
                _btnStudentDashSearch.performClick();
            }
        });

    }
}