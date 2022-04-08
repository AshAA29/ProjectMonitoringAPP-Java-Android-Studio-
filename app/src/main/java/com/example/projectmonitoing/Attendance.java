package com.example.projectmonitoing;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Attendance extends AppCompatActivity {
    ListView _attDisplay;
    TextView _attPercent;
    SQLiteOpenHelper openHelper2;
    ProgressBar _progressBarAtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        _attDisplay = (ListView) findViewById(R.id.attDisplay);
        openHelper2 = new DatabaseHelper(this);
        DatabaseHelper myDatabaseHelper =new DatabaseHelper(this);
        final ArrayList<String> attendanceList = myDatabaseHelper.getGroupMemberArray(login.getGroupNumber());
        ArrayAdapter attAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,attendanceList);
        _attDisplay.setAdapter(attAdapt);
        _attPercent = (TextView) findViewById(R.id.attPercent);
        _progressBarAtt = (ProgressBar) findViewById(R.id.progressBarAtt);

        _attDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    DatabaseHelper myDatabaseHelper2 =new DatabaseHelper(getApplicationContext());
                    String[] parts = attendanceList.get(position).split(" ");
                    String total = myDatabaseHelper2.getUserAttendanceTotal(parts[0],parts[1]);
                    String present = myDatabaseHelper2.getUserAttendancePresent(parts[0],parts[1]);

                    _progressBarAtt.setMax(Integer.parseInt(total));
                    _progressBarAtt.setProgress(Integer.parseInt(present));
                    _attPercent.setText(Integer.parseInt(present) + "/"+ Integer.parseInt(total) + "    " + round(((Double.parseDouble(present)/Double.parseDouble(total)) * 100),2) + "%" );
                }
                catch (Exception e)
                {
                    _progressBarAtt.setMax(0);
                    _progressBarAtt.setProgress(0);
                    _attPercent.setText("0/0"+"    " + "n/a" );
                }


            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}