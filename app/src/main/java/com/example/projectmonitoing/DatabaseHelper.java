package com.example.projectmonitoing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "register.db";
    public static final String TABLE_NAME = "register";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FirstName";
    public static final String COL_3 = "LastName";
    public static final String COL_5 = "Email";
    public static final String COL_6 = "Phone";
    public static final String COL_7 = "GroupNumber";

    public static final String TABLE_NAME2 = "progress";
    public static final String COL_11 = "ID2";
    public static final String COL_31 = "AgendaText";
    public static final String COL_41 = "Date";
    public static final String COL_51 = "GroupID";

    public static final String TABLE_NAME3 = "attendance";
    public static final String COL_111 = "ID3";
    public static final String COL_112 = "FirstName";
    public static final String COL_1122 = "LastName";
    public static final String COL_113 = "Status";
    public static final String COL_114 = "Date";
    public static final String COL_115 = "GroupID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FirstName TEXT, LastName TEXT, Email TEXT, Phone TEXT, GroupNumber TEXT )");
        db.execSQL("CREATE TABLE " + TABLE_NAME2 +
                "(ID2 INTEGER PRIMARY KEY AUTOINCREMENT, AgendaText TEXT, Date TEXT, GroupID TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME3 +
                "(ID3 INTEGER PRIMARY KEY AUTOINCREMENT, FirstName TEXT, LastName TEXT, Status TEXT, Date TEXT, GroupID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);  //Drop older table if it exists already
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);  //Drop older table if it exists already
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);  //Drop older table if it exists already
        onCreate(db);
    }
    @Override
        public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    public ArrayList<String> getGroupMemberArray(String groupNumber)
    {
        ArrayList<String> attendanceList = new ArrayList<>();

        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        //String whereclause = "ID=?";
        //String[] whereargs = new String[]{String.valueOf(id)};
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_7 + " =?", new String[]{groupNumber});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                attendanceList.add(csr.getString(csr.getColumnIndex(COL_2)) + " " + csr.getString(csr.getColumnIndex(COL_3)));
                csr.moveToNext();
            }
        }
        return attendanceList;

    }
    public String getGroupMembers(long id,String groupNumber) {
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_7 + " =?", new String[]{groupNumber});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                rv = rv +  csr.getString(csr.getColumnIndex(COL_2)) + " " + csr.getString(csr.getColumnIndex(COL_3)) +" \n";
                csr.moveToNext();
            }
        }
        return rv;
    }

    public String getLatestProgress(long id,String groupNumber) {

        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME2 + " WHERE " + DatabaseHelper.COL_51 + " =?", new String[]{groupNumber});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                rv = csr.getString(csr.getColumnIndex(COL_31));
                csr.moveToNext();
            }
        }
        return rv;
    }

    public String getUserAttendanceTotal(String FirstName, String LastName) {
        int counterTotal = 0;
        String rv = "1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME3 + " WHERE " + DatabaseHelper.COL_112 + " =? AND " + DatabaseHelper.COL_1122 + " =? ", new String[]{FirstName,LastName});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                counterTotal = counterTotal +1;
                csr.moveToNext();
            }
        }
        return String.valueOf(counterTotal);
    }

    public String getUserAttendancePresent(String FirstName, String LastName) {
        int counterPresent = 0;
        String rv = "1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME3 + " WHERE " + DatabaseHelper.COL_112 + " =?" + " AND " + DatabaseHelper.COL_1122 + " =? " + " AND " + DatabaseHelper.COL_113 + " =?", new String[]{FirstName,LastName,"Attended"});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                counterPresent = counterPresent +1;
                csr.moveToNext();
            }
        }
        return String.valueOf(counterPresent);
    }

    public ArrayList<String> getUserDetails (String FName, String LName) {
        ArrayList<String> StudentDetails = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COL_2 + " =?" +
                " AND " + DatabaseHelper.COL_3 + " =?", new String[]{FName,LName});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                StudentDetails.add(csr.getString(csr.getColumnIndex(COL_1)));
                StudentDetails.add(csr.getString(csr.getColumnIndex(COL_2)));
                StudentDetails.add(csr.getString(csr.getColumnIndex(COL_3)));
                StudentDetails.add(csr.getString(csr.getColumnIndex(COL_5)));
                StudentDetails.add(csr.getString(csr.getColumnIndex(COL_6)));
                StudentDetails.add(csr.getString(csr.getColumnIndex(COL_7)));
                csr.moveToNext();
            }
        }
        return StudentDetails;
    }

    public ArrayList<String> getFullStudentArray()
    {
        ArrayList<String> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME,null);
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                studentList.add(csr.getString(csr.getColumnIndex(COL_2)) + " " + csr.getString(csr.getColumnIndex(COL_3)));
                csr.moveToNext();
            }
        }
        return studentList;
    }

    public ArrayList<String> getGroupAgendaArray(String group)
    {
        ArrayList<String> AgendaList = new ArrayList<>();
        String rv = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor csr  = db.rawQuery(" SELECT * FROM " + DatabaseHelper.TABLE_NAME2 + " WHERE " + DatabaseHelper.COL_51 + " =?", new String[]{group});
        if (csr.moveToFirst()) {
            while(!csr.isAfterLast())
            {
                AgendaList.add(csr.getString(csr.getColumnIndex(COL_41)) + "\n" + csr.getString(csr.getColumnIndex(COL_31)));
                csr.moveToNext();
            }
        }
        return AgendaList;

    }

}
