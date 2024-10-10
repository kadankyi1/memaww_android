package com.memaww.memaww.Models;

public class SubscriptionModel {

    private static final long serialVersionUID = 1L;
    private long subscriptionId;
    private String subscriptionDays;
    private String subscriptionMonths;
    private long subscriptionCountryId;
    private long subscriptionAmtPerMonth;
    private long subscriptionAmtTotal;
    private String subscriptionPackageDescription1;
    private String subscriptionPackageDescription2;
    private String subscriptionPackageDescription3;
    private String subscriptionPackageDescription4;
    private String subscriptionAdderAdminName;
    private String createdAt;
    private String updatedAt;

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionDays() {
        return subscriptionDays;
    }

    public void setSubscriptionDays(String subscriptionDays) {
        this.subscriptionDays = subscriptionDays;
    }

    public String getSubscriptionMonths() {
        return subscriptionMonths;
    }

    public void setSubscriptionMonths(String subscriptionMonths) {
        this.subscriptionMonths = subscriptionMonths;
    }

    public long getSubscriptionCountryId() {
        return subscriptionCountryId;
    }

    public void setSubscriptionCountryId(long subscriptionCountryId) {
        this.subscriptionCountryId = subscriptionCountryId;
    }

    public long getSubscriptionAmtPerMonth() {
        return subscriptionAmtPerMonth;
    }

    public void setSubscriptionAmtPerMonth(long subscriptionAmtPerMonth) {
        this.subscriptionAmtPerMonth = subscriptionAmtPerMonth;
    }

    public long getSubscriptionAmtTotal() {
        return subscriptionAmtTotal;
    }

    public void setSubscriptionAmtTotal(long subscriptionAmtTotal) {
        this.subscriptionAmtTotal = subscriptionAmtTotal;
    }

    public String getSubscriptionPackageDescription1() {
        return subscriptionPackageDescription1;
    }

    public void setSubscriptionPackageDescription1(String subscriptionPackageDescription1) {
        this.subscriptionPackageDescription1 = subscriptionPackageDescription1;
    }

    public String getSubscriptionPackageDescription2() {
        return subscriptionPackageDescription2;
    }

    public void setSubscriptionPackageDescription2(String subscriptionPackageDescription2) {
        this.subscriptionPackageDescription2 = subscriptionPackageDescription2;
    }

    public String getSubscriptionPackageDescription3() {
        return subscriptionPackageDescription3;
    }

    public void setSubscriptionPackageDescription3(String subscriptionPackageDescription3) {
        this.subscriptionPackageDescription3 = subscriptionPackageDescription3;
    }

    public String getSubscriptionPackageDescription4() {
        return subscriptionPackageDescription4;
    }

    public void setSubscriptionPackageDescription4(String subscriptionPackageDescription4) {
        this.subscriptionPackageDescription4 = subscriptionPackageDescription4;
    }

    public String getSubscriptionAdderAdminName() {
        return subscriptionAdderAdminName;
    }

    public void setSubscriptionAdderAdminName(String subscriptionAdderAdminName) {
        this.subscriptionAdderAdminName = subscriptionAdderAdminName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
