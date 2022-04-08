package com.example.projectmonitoing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentUpdate extends AppCompatActivity {
    EditText _txtfname, _txtlname, _txtemail, _txtphone, _txtgroupnumber;
    Button _btnStudentUpdate,_btnStudentDashRemoveGroup,_btnStudentDashDelete;
    SQLiteOpenHelper openHelper2;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);
        _txtfname = (EditText) findViewById(R.id.txtfname4);
        _txtlname = (EditText) findViewById(R.id.txtlname4);
        _txtphone = (EditText) findViewById(R.id.txtphone4);
        _txtemail = (EditText) findViewById(R.id.txtemail4);
        _txtgroupnumber = (EditText) findViewById(R.id.txtgroup);
        _btnStudentUpdate = (Button) findViewById(R.id.btnStudentUpdate);
        _btnStudentDashRemoveGroup = (Button) findViewById(R.id.btnStudentDashRemoveGroup);
        _btnStudentDashDelete = (Button) findViewById(R.id.btnStudentDashDelete);

        openHelper2 = new DatabaseHelper(this);
        String name = StudentDashboard.getFullName();
        String[] parts = name.split(" ");
        String fname = parts[0];
        String lname = parts[1];
        db = openHelper2.getReadableDatabase();
        DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);
        final ArrayList<String> userDetails = myDatabaseHelper.getUserDetails(fname,lname);
        _txtfname.setHint(userDetails.get(1));
        _txtlname.setHint(userDetails.get(2));
        _txtphone.setHint(userDetails.get(3));
        _txtemail.setHint(userDetails.get(4));
        _txtgroupnumber.setHint(userDetails.get(5));

        _btnStudentUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName,lastName,phone,email,group;
                if(_txtfname.getText().toString().trim().isEmpty()) { firstName = userDetails.get(1); } else { firstName = _txtfname.getText().toString(); }
                if(_txtlname.getText().toString().trim().isEmpty()) { lastName = userDetails.get(2); } else { lastName = _txtlname.getText().toString(); }
                if(_txtphone.getText().toString().trim().isEmpty()) { phone = userDetails.get(3); } else { phone = _txtphone.getText().toString(); }
                if(_txtemail.getText().toString().trim().isEmpty()) { email = userDetails.get(4); } else { email = _txtemail.getText().toString(); }
                if(_txtgroupnumber.getText().toString().trim().isEmpty()) { group = userDetails.get(5); } else { group = _txtgroupnumber.getText().toString(); }

                ContentValues cv = new ContentValues();
                cv.put("FirstName",firstName);
                cv.put("LastName",lastName);
                cv.put("Email",phone);
                cv.put("Phone",email);
                cv.put("GroupNumber",group);

                db.beginTransaction();
                try {
                    db.update("register", cv, DatabaseHelper.COL_1 + " = ?", new String[]{userDetails.get(0)});
                    db.setTransactionSuccessful();
                    Toast.makeText(getApplicationContext(), "Successfully Updated",Toast.LENGTH_SHORT).show();
                } finally {
                    db.endTransaction();
                }
                Intent intent = new Intent(StudentUpdate.this, login.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        _btnStudentDashRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentUpdate.this);
            builder.setMessage("Are you sure you want to remove student from its group?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues cv = new ContentValues();
                            cv.put("GroupNumber","TempGroup");

                            db.beginTransaction();
                            try {
                                db.update("register", cv, DatabaseHelper.COL_1 + " = ?", new String[]{userDetails.get(0)});
                                db.setTransactionSuccessful();
                                Toast.makeText(getApplicationContext(), "Successfully removed from group",Toast.LENGTH_SHORT).show();
                            } finally {
                                db.endTransaction();
                            }
                            Intent intent = new Intent(StudentUpdate.this, login.class);
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            }
        });

        _btnStudentDashDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentUpdate.this);
                builder.setMessage("Are you sure you want to delete student?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.beginTransaction();
                                try {
                                    db.delete("register", DatabaseHelper.COL_1 + " = ?", new String[]{userDetails.get(0)});
                                    db.setTransactionSuccessful();
                                    Toast.makeText(getApplicationContext(), "Successfully deleted",Toast.LENGTH_SHORT).show();
                                } finally {
                                    db.endTransaction();
                                }
                                Intent intent = new Intent(StudentUpdate.this, login.class);
                                finish();
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
}