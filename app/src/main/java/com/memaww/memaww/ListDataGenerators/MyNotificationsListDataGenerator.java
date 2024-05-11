package com.memaww.memaww.ListDataGenerators;

import com.memaww.memaww.Models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class MyNotificationsListDataGenerator {

    // DECLARING THE DATA ARRAY LIST
    static List<NotificationModel> allMyNotificationsData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllMyNotificationsDatasAfresh(List<NotificationModel> newAllData) {
        MyNotificationsListDataGenerator.allMyNotificationsData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(NotificationModel model) {
        return allMyNotificationsData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<NotificationModel> getAllData() {
        return allMyNotificationsData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, NotificationModel model){
        allMyNotificationsData.add(i, model);
    }
}
