package com.project01.bankaccountmanagement.model;

public class Customer {
    private int customerID;
    private String fullName;
    private String identityCard;
    private String phone;
    private String email;
    private String address;

    public Customer() {}

    public Customer(int customerID, String fullName, String identityCard, String phone, String email, String address) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Customer(String fullName, String identityCard, String phone, String email, String address) {
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getIdentityCard() { return identityCard; }
    public void setIdentityCard(String identityCard) { this.identityCard = identityCard; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    @Override
    public String toString() {
        return customerID + " - " + fullName;
    }
}