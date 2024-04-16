package com.memaww.memaww.ListDataGenerators;

import com.memaww.memaww.Models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MyMessagesListDataGenerator {


    // DECLARING THE DATA ARRAY LIST
    static List<MessageModel> allMyMessagesData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllMyMessagesDatasAfresh(List<MessageModel> newAllData) {
        MyMessagesListDataGenerator.allMyMessagesData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(MessageModel model) {
        return allMyMessagesData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<MessageModel> getAllData() {
        return allMyMessagesData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, MessageModel model){
        allMyMessagesData.add(i, model);
    }
}
