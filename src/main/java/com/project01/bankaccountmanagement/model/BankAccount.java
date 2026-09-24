package com.project01.bankaccountmanagement.model;

import java.sql.Timestamp;

public class BankAccount {
    private int accountID;
    private int customerID;
    private String accountNumber;
    private double balance;
    private String status;
    private Timestamp createdAt;

    public BankAccount() {}

    public BankAccount(int accountID, int customerID, String accountNumber, double balance, String status, Timestamp createdAt) {
        this.accountID = accountID;
        this.customerID = customerID;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getAccountID() { return accountID; }
    public void setAccountID(int accountID) { this.accountID = accountID; }

    public int getCustomerID() { return customerID; }
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}