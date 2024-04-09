package com.memaww.memaww.ListDataGenerators;

import com.memaww.memaww.Models.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersListDataGenerator {


    // DECLARING THE DATA ARRAY LIST
    static List<OrderModel> allMyOrdersData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllMyOrdersDatasAfresh(List<OrderModel> newAllData) {
        MyOrdersListDataGenerator.allMyOrdersData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(OrderModel model) {
        return allMyOrdersData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<OrderModel> getAllData() {
        return allMyOrdersData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, OrderModel model){
        allMyOrdersData.add(i, model);
    }
}
