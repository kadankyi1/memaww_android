package com.memaww.memaww.Models;

public class NotificationModel {
    private static final long serialVersionUID = 1L;
    private String notificationUserId;
    private String notificationTitle;
    private String notificationBody;
    private String notificationDate;

    public String getNotificationUserId() {
        return notificationUserId;
    }

    public void setNotificationUserId(String notificationUserId) {
        this.notificationUserId = notificationUserId;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}
