package com.memaww.memaww.ListDataGenerators;

import com.memaww.memaww.Models.RemainingLaundryModel;

import java.util.ArrayList;
import java.util.List;

public class RemainingLaundryListDataGenerator {

    // DECLARING THE DATA ARRAY LIST
    static List<RemainingLaundryModel> allRemainingLaundryData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setRemainingLaundryDataAfresh(List<RemainingLaundryModel> newAllData) {
        RemainingLaundryListDataGenerator.allRemainingLaundryData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(RemainingLaundryModel model) {
        return allRemainingLaundryData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<RemainingLaundryModel> getAllData() {
        return allRemainingLaundryData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, RemainingLaundryModel model){
        allRemainingLaundryData.add(i, model);
    }
}
