package com.example.sentinel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private static SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.APPNAME, name);
        contentValue.put(DatabaseHelper.TITLE, desc);
        database.insert(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, null, contentValue);
    }

    public void insertNotification(NotificationObject notificationObject) {
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(DatabaseHelper.APPNAME, notificationObject.getAppName());
            contentValue.put(DatabaseHelper.TITLE, notificationObject.getTitle());
            contentValue.put(DatabaseHelper.DESC, notificationObject.getDesc());
            contentValue.put(DatabaseHelper.TIMESTAMP, notificationObject.getTimeStamp());
            contentValue.put(DatabaseHelper.PRIORITY, notificationObject.getPriority());
            contentValue.put(DatabaseHelper.ICON, notificationObject.getImageBytes());
            database.insert(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, null, contentValue);
//            insertNewApplication(notificationObject);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("lastEventObject", "Excep");
        }
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.APPNAME, DatabaseHelper.TITLE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchNotif() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.APPNAME, DatabaseHelper.TITLE, DatabaseHelper.DESC, DatabaseHelper.TIMESTAMP, DatabaseHelper.PRIORITY};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.APPNAME, name);
        contentValues.put(DatabaseHelper.TITLE, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_NOTIFICATIONS, DatabaseHelper._ID + "=" + _id, null);
    }

    public void getFirst() {
        Cursor cursor = fetch();
        try {
            if (cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        String s = cursor.getString(cursor.getColumnIndex(DatabaseHelper.APPNAME));
//                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<NotificationObject> getNotifications() {
        Cursor cursor = fetchNotif();
        ArrayList<NotificationObject> notificationObjects = new ArrayList<>();
        Log.e("getNotifications", "Count -- " + cursor.getCount());
        try {
            if (cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        NotificationObject notificationObject = new NotificationObject();
                        String s = cursor.getString(cursor.getColumnIndex(DatabaseHelper.APPNAME));
                        notificationObject.setAppName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.APPNAME)));
                        notificationObject.setDesc(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESC)));
                        notificationObject.setTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIMESTAMP)));
                        notificationObjects.add(notificationObject);
//                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            Collections.reverse(notificationObjects);
            return notificationObjects;
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
        }
        return notificationObjects;
    }

    public ArrayList<NotificationObject> getAllNotifications() {
        ArrayList<NotificationObject> notificationObjects = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_NAME_APPLICATIONS, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.e("getAllNotifications", "Tryin");
                NotificationObject notificationObject = new NotificationObject();
                String appname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.APPNAME));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESC));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIMESTAMP));
                String priority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PRIORITY));
                byte[] icon = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.ICON));
                notificationObject.setAppName(appname);
                notificationObject.setTitle(title);
                notificationObject.setDesc(desc);
                notificationObject.setTimeStamp(time);
                notificationObject.setPriority(priority);
                notificationObject.setImageBytes(icon);
                notificationObjects.add(notificationObject);
                cursor.moveToNext();
                Log.e("getAllNotifications", "sucseeded");
            }
        }
        Log.e("getAllNotifications", "Count -- " + notificationObjects.size());
        return notificationObjects;
    }
}
