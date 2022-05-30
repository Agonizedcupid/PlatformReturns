package com.aariyan.platformreturns.Model;

public class StoreModel {
    private String deliveryDate;
    private int routeId;
    private String customerId;
    private String customerCode;
    private String storeName;

    public StoreModel(){}

    public StoreModel(String deliveryDate, int routeId, String customerId, String customerCode, String storeName) {
        this.deliveryDate = deliveryDate;
        this.routeId = routeId;
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.storeName = storeName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
