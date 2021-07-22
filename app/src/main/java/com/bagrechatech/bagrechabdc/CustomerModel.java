package com.bagrechatech.bagrechabdc;

public class CustomerModel {
    String stationName;
    String customerName;
    String contactNumber;
    String customerAddress;
    String customerRemarks;
    String bussinessCardUrl;

    public CustomerModel() {
        
    }

    public CustomerModel(String stationName, String customerName, String contactNumber, String customerAddress, String customerRemarks, String bussinessCardUrl) {
        this.stationName = stationName;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.customerAddress = customerAddress;
        this.customerRemarks = customerRemarks;
        this.bussinessCardUrl = bussinessCardUrl;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerRemarks() {
        return customerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        this.customerRemarks = customerRemarks;
    }

    public String getBussinessCardUrl() {
        return bussinessCardUrl;
    }

    public void setBussinessCardUrl(String bussinessCardUrl) {
        this.bussinessCardUrl = bussinessCardUrl;
    }
}
