package com.example.sentinel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

public class Master extends Application {
    public static Context mContext;
    String MY_PREFS_NAME = "com.example.sentinel.preferences";
    static DBManager dbManager;

    public static DBManager getDataBase() {
        if (dbManager == null) {
            dbManager = new DBManager(mContext);
            dbManager.open();
        }
        return dbManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initializeDatabase();
        registerNotificationListenerService();
    }

    private void initializeDatabase() {
        dbManager = new DBManager(mContext);
        dbManager.open();
       /* NotificationObject notificationObject = new NotificationObject("App name", "New message", "Message content", "PM", "High");
        dbManager.insertNotification(notificationObject);*/
        dbManager.getNotifications();
//        Toast.makeText(mContext, "Initialized", Toast.LENGTH_SHORT).show();
    }

    private void registerNotificationListenerService() {
        Intent serviceIntent = new Intent(mContext, NLService.class);
        try {
            mContext.startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(final String msg) {
        Log.e("Showtoast Master", msg);
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void getNotificationAccess() {
        try {
            if (!Settings.Secure.getString(mContext.getContentResolver(), "enabled_notification_listeners").contains(mContext.getPackageName())) {
                Toast.makeText(mContext, "Enable " + mContext.getResources().getString(R.string.app_name) + " here", Toast.LENGTH_LONG).show();
                mContext.startActivity(new Intent(
                        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (!NotificationManagerCompat.getEnabledListenerPackages(mContext).contains(mContext.getPackageName())) {
                    Toast.makeText(mContext, "Enable " + mContext.getResources().getString(R.string.app_name) + " here", Toast.LENGTH_LONG).show();
                    mContext.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            } catch (Resources.NotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void writePref(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readPref(String key) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, "");
    }
}
