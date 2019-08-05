package com.example.sentinel;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class NLService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    Long longDate;
    String notifDesc, notifTitle;
    Date time;
    String date, notifTimeStamp;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "**********  OnstartCommand ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onListenerConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActiveNotifications();
        }
        Log.d(TAG, "Listener connected");
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onListenerDisconnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestRebind(new ComponentName(this, NotificationListenerService.class));
        }
    }

    private Date stringToDate(String aDate) {
        Log.e("StringToDate adate", aDate);
        String aFormat = getString(R.string.full_date_time);
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        Log.e("StringToDate", stringDate.toString());
        return stringDate;
    }

    private Integer getDifferenceInt(Date startDate, Date endDate) {
        String returnString = "N/A";
        long different = endDate.getTime() - startDate.getTime();
        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;
        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;
        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long elapsedSeconds = different / secondsInMilli;
        System.out.printf(
                "%d day(s), %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        if (elapsedDays != 0) {
            returnString = String.valueOf(elapsedDays) + " days " +
                    String.valueOf(elapsedHours) + " hours " +
                    String.valueOf(elapsedMinutes) + " mins " + "to go";
        } else if (elapsedHours != 0) {
            returnString = String.valueOf(elapsedHours) + " hours " +
                    String.valueOf(elapsedMinutes) + " mins " + "to go";
        } else {
            returnString =
                    String.valueOf(elapsedMinutes) + " mins " +
                            String.valueOf(elapsedSeconds) + " seconds to go";
        }
        if (returnString.contains("-")) {
            returnString = returnString.replaceAll("-", "");
            returnString = "Triggered " + returnString.replace("to go", "ago");
        }
        return (int) elapsedMinutes;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "**********  onNotificationPosted");
        Log.e("onNotificationPosted", "**********  onNotificationPosted");
        Intent i = new Intent("com.essential.assistant.preferences");
        Bundle extras = sbn.getNotification().extras;
        if (extras != null) {
            Log.i(TAG, "onNotificationPosted :" + " Inside");
            try {
                notifTitle = extras.getString(Notification.EXTRA_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
                notifTitle = "No notifTitle";
            }
//                notifTitle = extras.getString(Notification.EXTRA_);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String channel_id = extras.getString(Notification.EXTRA_CHANNEL_ID);
                    Log.e("Channel id", channel_id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            notifDesc = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
            time = Calendar.getInstance().getTime();
            longDate = time.getTime();
                /*i.putExtra("notification_event", notifTitle + " " + "\n");
                sendBroadcast(i);*/
            Date d = new Date();
            d.setTime(longDate);
            String stringTime = dateToString(d);
            date = stringTime.substring(0, 10);
            notifTimeStamp = stringTime.substring(11);
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            String timestampshort = sdf.format(dt);
            Log.e("onNotificationPosted", "Title  : " + notifTitle);
            Log.e("onNotificationPosted", "Text  : " + notifDesc);
            Log.e("onNotificationPosted", "Time  : " + notifTimeStamp);
            Log.e("onNotificationPosted", "onNotificationPosted :" + " Master");
            String pack = sbn.getPackageName();
            final PackageManager pm = getApplicationContext().getPackageManager();
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(pack, 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
//            Toast.makeText(this, applicationName, Toast.LENGTH_SHORT).show();
            Log.e("applicationName", applicationName);
            Log.e("lastEventObject", "trying");
            int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
            Bitmap id = sbn.getNotification().largeIcon;
            NotificationObject notificationObject = new NotificationObject(applicationName, notifTitle, notifDesc, timestampshort, "High", null);
            DBManager dbManager = Master.getDataBase();
            Log.e("lastEventObject", "Inserted");
            dbManager.insertNotification(notificationObject);
//        Master.showToast("onNotificationPosted");
        } else {
            Log.e("lastEventObject", "Same as before");
            Log.i("onNotificationPosted", "onNotificationPosted :" + " Inside2");
            i.putExtra("notification_event", "onNotificationPosted :" + "Extras null");
            Log.i("onNotificationPosted", "onNotificationPosted :" + "Extras null");
            sendBroadcast(i);
        }
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private boolean isContaining(String word) {
        return Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(notifDesc).find();
    }

    public String dateToString(Date d) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String dateTime = "N/A";
        try {
            dateTime = dateFormat.format(d);
            System.out.println("Current Date Time : " + dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
