package com.example.sentinel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_NAME_NOTIFICATIONS = "table_notifications";
    public static final String TABLE_NAME_APPLICATIONS = "table_applications";
    // Table columns
    public static final String _ID = "_id";
    public static final String APPNAME = "app_name";
    public static final String TITLE = "title";
    public static final String DESC = "description";
    public static final String TIMESTAMP = "timestamp";
    public static final String PRIORITY = "priority";
    public static final String ICON = "appIcon";
    // Database Information
    static final String DB_NAME = "sentinel_database";
    // database version
    static final int DB_VERSION = 1;
    // Creating table query
    private static final String CREATE_TABLE_NOTIFICATIONS = "create table " + TABLE_NAME_NOTIFICATIONS + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + APPNAME + " TEXT NOT NULL, "
            + TITLE + " TEXT NOT NULL, "
            + DESC + " TEXT NOT NULL, "
            + TIMESTAMP + " TEXT NOT NULL, "
            + PRIORITY + " TEXT NOT NULL, "
            + ICON + " BLOB);";
    private static final String CREATE_TABLE_APPLICATION_LIST = "create table " + TABLE_NAME_APPLICATIONS + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + APPNAME + " TEXT NOT NULL, "
            + ICON + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTIFICATIONS);
        db.execSQL(CREATE_TABLE_APPLICATION_LIST);
        Log.e("onCreate", CREATE_TABLE_NOTIFICATIONS);
        Log.e("onCreate", CREATE_TABLE_APPLICATION_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTIFICATIONS);
        onCreate(db);
    }
}
