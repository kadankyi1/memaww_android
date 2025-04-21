package com.memaww.memaww.Models;

public class OrderModel {

    private static final long serialVersionUID = 1L;
    private long orderId;
    private String orderSysId;
    private long orderUserId;
    private long orderLaundryspId;
    private String orderCollectionBikerName;
    private String orderCollectionLocationRaw;
    private String orderCollectionLocationGps;
    private String orderCollectionDate;
    private String orderCollectionContactPersonPhone;
    private String orderDropoffLocationRaw;
    private String orderDropoffLocationGps;
    private String orderDropoffDate;
    private String orderDropoffContactPersonPhone;
    private String orderDropoffBikerName;
    private int orderLightweightitemsJustWashQuantity;
    private int orderLightweightitemsWashAndIronQuantity;
    private int orderBulkyitemsJustWashQuantity;
    private int orderBulkyitemsWashAndIronQuantity;
    private String orderItemsDescription;
    private String orderAllItemsQuantity;
    private int orderBeingWorkedOnStatus;
    private int orderPaymentStatus;
    private String orderPaymentStatusMessage;
    private String orderPaymentDetails;
    private String orderFinalPrice;
    private String orderPriceCurrency;
    private int orderFlagged;
    private String orderFlaggedReason;
    private String createdAt;
    private String createdAtShortDate;
    private String updatedAt;
    private String orderIdLong;
    private String orderDeliveryDate;
    private int orderStatusNumberForProgressBar;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderSysId() {
        return orderSysId;
    }

    public void setOrderSysId(String orderSysId) {
        this.orderSysId = orderSysId;
    }

    public long getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(long orderUserId) {
        this.orderUserId = orderUserId;
    }

    public long getOrderLaundryspId() {
        return orderLaundryspId;
    }

    public void setOrderLaundryspId(long orderLaundryspId) {
        this.orderLaundryspId = orderLaundryspId;
    }

    public String getOrderCollectionBikerName() {
        return orderCollectionBikerName;
    }

    public void setOrderCollectionBikerName(String orderCollectionBikerName) {
        this.orderCollectionBikerName = orderCollectionBikerName;
    }

    public String getOrderCollectionLocationRaw() {
        return orderCollectionLocationRaw;
    }

    public void setOrderCollectionLocationRaw(String orderCollectionLocationRaw) {
        this.orderCollectionLocationRaw = orderCollectionLocationRaw;
    }

    public String getOrderCollectionLocationGps() {
        return orderCollectionLocationGps;
    }

    public void setOrderCollectionLocationGps(String orderCollectionLocationGps) {
        this.orderCollectionLocationGps = orderCollectionLocationGps;
    }

    public String getOrderCollectionDate() {
        return orderCollectionDate;
    }

    public void setOrderCollectionDate(String orderCollectionDate) {
        this.orderCollectionDate = orderCollectionDate;
    }

    public String getOrderCollectionContactPersonPhone() {
        return orderCollectionContactPersonPhone;
    }

    public void setOrderCollectionContactPersonPhone(String orderCollectionContactPersonPhone) {
        this.orderCollectionContactPersonPhone = orderCollectionContactPersonPhone;
    }

    public String getOrderDropoffLocationRaw() {
        return orderDropoffLocationRaw;
    }

    public void setOrderDropoffLocationRaw(String orderDropoffLocationRaw) {
        this.orderDropoffLocationRaw = orderDropoffLocationRaw;
    }

    public String getOrderDropoffLocationGps() {
        return orderDropoffLocationGps;
    }

    public void setOrderDropoffLocationGps(String orderDropoffLocationGps) {
        this.orderDropoffLocationGps = orderDropoffLocationGps;
    }

    public String getOrderDropoffDate() {
        return orderDropoffDate;
    }

    public void setOrderDropoffDate(String orderDropoffDate) {
        this.orderDropoffDate = orderDropoffDate;
    }

    public String getOrderDropoffContactPersonPhone() {
        return orderDropoffContactPersonPhone;
    }

    public void setOrderDropoffContactPersonPhone(String orderDropoffContactPersonPhone) {
        this.orderDropoffContactPersonPhone = orderDropoffContactPersonPhone;
    }

    public String getOrderDropoffBikerName() {
        return orderDropoffBikerName;
    }

    public void setOrderDropoffBikerName(String orderDropoffBikerName) {
        this.orderDropoffBikerName = orderDropoffBikerName;
    }


    public int getOrderPaymentStatus() {
        return orderPaymentStatus;
    }

    public void setOrderPaymentStatus(int orderPaymentStatus) {
        this.orderPaymentStatus = orderPaymentStatus;
    }

    public String getOrderPaymentDetails() {
        return orderPaymentDetails;
    }

    public void setOrderPaymentDetails(String orderPaymentDetails) {
        this.orderPaymentDetails = orderPaymentDetails;
    }

    public int getOrderLightweightitemsJustWashQuantity() {
        return orderLightweightitemsJustWashQuantity;
    }

    public void setOrderLightweightitemsJustWashQuantity(int orderLightweightitemsJustWashQuantity) {
        this.orderLightweightitemsJustWashQuantity = orderLightweightitemsJustWashQuantity;
    }

    public int getOrderLightweightitemsWashAndIronQuantity() {
        return orderLightweightitemsWashAndIronQuantity;
    }

    public void setOrderLightweightitemsWashAndIronQuantity(int orderLightweightitemsWashAndIronQuantity) {
        this.orderLightweightitemsWashAndIronQuantity = orderLightweightitemsWashAndIronQuantity;
    }

    public int getOrderBulkyitemsJustWashQuantity() {
        return orderBulkyitemsJustWashQuantity;
    }

    public void setOrderBulkyitemsJustWashQuantity(int orderBulkyitemsJustWashQuantity) {
        this.orderBulkyitemsJustWashQuantity = orderBulkyitemsJustWashQuantity;
    }

    public int getOrderBulkyitemsWashAndIronQuantity() {
        return orderBulkyitemsWashAndIronQuantity;
    }

    public void setOrderBulkyitemsWashAndIronQuantity(int orderBulkyitemsWashAndIronQuantity) {
        this.orderBulkyitemsWashAndIronQuantity = orderBulkyitemsWashAndIronQuantity;
    }

    public String getOrderItemsDescription() {
        return orderItemsDescription;
    }

    public void setOrderItemsDescription(String orderItemsDescription) {
        this.orderItemsDescription = orderItemsDescription;
    }

    public int getOrderBeingWorkedOnStatus() {
        return orderBeingWorkedOnStatus;
    }

    public void setOrderBeingWorkedOnStatus(int orderBeingWorkedOnStatus) {
        this.orderBeingWorkedOnStatus = orderBeingWorkedOnStatus;
    }

    public int getOrderFlagged() {
        return orderFlagged;
    }

    public void setOrderFlagged(int orderFlagged) {
        this.orderFlagged = orderFlagged;
    }

    public String getOrderFlaggedReason() {
        return orderFlaggedReason;
    }

    public void setOrderFlaggedReason(String orderFlaggedReason) {
        this.orderFlaggedReason = orderFlaggedReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtShortDate() {
        return createdAtShortDate;
    }

    public void setCreatedAtShortDate(String createdAtShortDate) {
        this.createdAtShortDate = createdAtShortDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOrderAllItemsQuantity() {
        return orderAllItemsQuantity;
    }

    public void setOrderAllItemsQuantity(String orderAllItemsQuantity) {
        this.orderAllItemsQuantity = orderAllItemsQuantity;
    }

    public String getOrderPaymentStatusMessage() {
        return orderPaymentStatusMessage;
    }

    public void setOrderPaymentStatusMessage(String orderPaymentStatusMessage) {
        this.orderPaymentStatusMessage = orderPaymentStatusMessage;
    }

    public String getOrderFinalPrice() {
        return orderFinalPrice;
    }

    public void setOrderFinalPrice(String orderFinalPrice) {
        this.orderFinalPrice = orderFinalPrice;
    }

    public String getOrderPriceCurrency() {
        return orderPriceCurrency;
    }

    public void setOrderPriceCurrency(String orderPriceCurrenty) {
        this.orderPriceCurrency = orderPriceCurrenty;
    }

    public String getOrderIdLong() {
        return orderIdLong;
    }

    public void setOrderIdLong(String orderIdLong) {
        this.orderIdLong = orderIdLong;
    }

    public String getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public void setOrderDeliveryDate(String orderDeliveryDate) {
        this.orderDeliveryDate = orderDeliveryDate;
    }

    public int getOrderStatusNumberForProgressBar() {
        return orderStatusNumberForProgressBar;
    }

    public void setOrderStatusNumberForProgressBar(int orderStatusNumberForProgressBar) {
        this.orderStatusNumberForProgressBar = orderStatusNumberForProgressBar;
    }
}
