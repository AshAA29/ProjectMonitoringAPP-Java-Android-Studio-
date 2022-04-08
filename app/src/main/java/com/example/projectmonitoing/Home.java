package com.example.projectmonitoing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;
import java.util.ArrayList;


public class Home extends AppCompatActivity
{
    private int STORAGE_PREMISSION_CODE = 1;
    TextView _userDisplay,_groupDisplay,_txtLastWeek;
    Button _makeAgenda,_DownloadLog,_btnAccessAtt,_btnProgressReview;
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;
    ListView _userDisplayList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);
        _groupDisplay = (TextView) findViewById(R.id.groupDisplay);
        _txtLastWeek = (TextView) findViewById(R.id.txtLastWeek);
        _makeAgenda = (Button) findViewById(R.id.makeAgenda);
        _DownloadLog = (Button) findViewById(R.id.btnDownloadLogs);
        _btnAccessAtt = (Button) findViewById(R.id.btnAccessAtt);
        _userDisplayList = (ListView) findViewById(R.id.userDisplayList);
        _btnProgressReview = (Button) findViewById(R.id.btnProgressReview);


        final ArrayList<String> userList = myDatabaseHelper.getGroupMemberArray(login.getGroupNumber());
        ArrayAdapter UserList = new ArrayAdapter(this, android.R.layout.simple_list_item_1,userList);
        _userDisplayList.setAdapter(UserList);

        //_userDisplay.setText("Group Members: \n" + login.getGroupMember());
        _groupDisplay.setText("Group: " + login.getGroupNumber());
        openHelper = new DatabaseHelper(this);
        db = openHelper.getReadableDatabase();
        _txtLastWeek.setText(myDatabaseHelper.getLatestProgress(1,login.getGroupNumber()));

        _makeAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Home.this, MakeAgenda.class);
                startActivity(intent);
                finish();
            }
        });

        _DownloadLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Home.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    String testPath = "";

                    if (Build.VERSION.SDK_INT > 30) {
                        testPath = "/sdcard/Download";
                    } else {
                        testPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                    }



                    SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), myDatabaseHelper.getDatabaseName(),testPath);
                    sqliteToExcel.exportSingleTable("progress","MyLogData.xls", new SQLiteToExcel.ExportListener() {
                        @Override
                        public void onStart() {
                            Toast.makeText(Home.this, "downloading..." , Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCompleted(String filePath) {
                            Toast.makeText(Home.this, "Exported:" + filePath , Toast.LENGTH_SHORT).show();
                            //_txtLastWeek.setText(filePath);
                        }
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(Home.this, e.toString(), Toast.LENGTH_LONG).show();
                            //_txtLastWeek.setText(e.toString());
                        }
                    });

                }

                else {
                    requestStoragePermission();
                }

            }
        });

        _btnAccessAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Attendance.class);
                startActivity(intent);

            }
        });

        _btnProgressReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ProgressReview.class);
                startActivity(intent);
            }
        });

    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PREMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PREMISSION_CODE);
        }
    }

}

