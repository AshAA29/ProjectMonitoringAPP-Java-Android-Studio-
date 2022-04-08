package com.example.projectmonitoing;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    Button _btnLogin,_btnRegister,_btnStudentMod;
    EditText _txtEmail, _txtPass,_txtGroupID;
    Cursor cursor,userCheck;

    public static String getUsername() {
        return username;
    }
    public static String username = "";

    public static String getGroupNumber() {
        return groupNumber;
    }
    public static String groupNumber = "";

    public static String getGroupMember() {
        return groupMember;
    }
    public static String groupMember = "";

    public static String getlatestAgenda() {
        return latestAgenda;
    }
    public static String latestAgenda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        _btnLogin = (Button) findViewById(R.id.btnLogin);
        _btnRegister = (Button) findViewById(R.id.btnRegister);
        _txtGroupID = (EditText) findViewById(R.id.txtGroupID);
        _btnStudentMod = (Button) findViewById(R.id.btnStudentMod);

        final DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);

        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group= _txtGroupID.getText().toString();

                groupNumber = group;
                groupMember = myDatabaseHelper.getGroupMembers(1,group);
                latestAgenda = myDatabaseHelper.getLatestProgress(1,group);

                cursor = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_7 + " =? ", new String[]{group});
                if(cursor != null)
                {
                    if(cursor.getCount()>0)
                    {
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(), "Group found!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, Home.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Group not found", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        _btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        _btnStudentMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, StudentDashboard.class);
                startActivity(intent);
            }
        });
    }
}
