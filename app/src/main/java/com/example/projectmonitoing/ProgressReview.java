package com.example.projectmonitoing;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ProgressReview extends AppCompatActivity {
    SQLiteOpenHelper openHelper2;
    ListView _progressDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_review);
        _progressDateList = (ListView) findViewById(R.id.progressDateList);
        openHelper2 = new DatabaseHelper(this);
        DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);
        final ArrayList<String> progressList = myDatabaseHelper.getGroupAgendaArray(login.getGroupNumber());
        ArrayAdapter attAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,progressList);
        _progressDateList.setAdapter(attAdapt);
    }
}