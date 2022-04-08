package com.example.projectmonitoing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button _btnreg, _btnlog, _btnStudentMod;
    EditText _txtfname, _txtlname, _txtpass, _txtemail, _txtphone, _txtgroupnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openHelper = new DatabaseHelper(this);
        _btnreg = (Button) findViewById(R.id.btnreg);
        _txtfname = (EditText) findViewById(R.id.txtfname);
        _txtlname = (EditText) findViewById(R.id.txtlname);
        _txtemail = (EditText) findViewById(R.id.txtemail);
        _txtphone = (EditText) findViewById(R.id.txtphone);
        _btnlog = (Button) findViewById(R.id.btnlog);
        _txtgroupnumber = (EditText) findViewById(R.id.txtGroupNumber);



        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();
                String fname = _txtfname.getText().toString();
                String lname = _txtlname.getText().toString();
                String email = _txtemail.getText().toString();
                String phone = _txtphone.getText().toString();
                String GroupNumber = _txtgroupnumber.getText().toString();

                if(_txtfname.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "You have missed a compulsory field (First name, Last name or Group)", Toast.LENGTH_SHORT).show();
                }
                else if (_txtlname.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You have missed a compulsory field (First name, Last name or Group)", Toast.LENGTH_SHORT).show();
                }
                else if (_txtgroupnumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You have missed a compulsory field (First name, Last name or Group)", Toast.LENGTH_SHORT).show();
                }
                else{
                    insertData(MakeUpperCase(fname),MakeUpperCase(lname),email,phone,GroupNumber);
                    Toast.makeText(getApplicationContext(), "You have successfully registered a new student", Toast.LENGTH_SHORT).show();
                }




            }
        });
        _btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });



    }
    public void insertData(String fname, String lname,  String email, String phone, String GroupNumber)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2, fname);
        contentValues.put(DatabaseHelper.COL_3, lname);
        contentValues.put(DatabaseHelper.COL_5, email);
        contentValues.put(DatabaseHelper.COL_6, phone);
        contentValues.put(DatabaseHelper.COL_7, GroupNumber);
        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);


    }

    public static String MakeUpperCase(String mystr) {
        if(mystr == null || mystr.isEmpty()) { return mystr; }
        return mystr.substring(0, 1).toUpperCase() + mystr.substring(1).toLowerCase();
    }
}
