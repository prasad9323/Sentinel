package com.example.sentinel;

public class NotificationObject {
    String AppName;
    String Title;
    String Desc;
    String TimeStamp;
    String Priority;

    public NotificationObject() {

    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    byte[] imageBytes;
    public NotificationObject(String app_name, String title, String desc, String timestamp, String priority, byte[] imageBytes) {
        this.AppName = app_name;
        this.Title = title;
        this.Desc = desc;
        this.TimeStamp = timestamp;
        this.Priority = priority;
        this.imageBytes = imageBytes;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }
}
