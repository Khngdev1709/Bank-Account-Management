package com.project01.bankaccountmanagement.model;

public class AdminUser {
    private int adminID;
    private String username;
    private String passwordHash;
    private String fullName;
    private boolean isActive;

    public AdminUser() {}

    public AdminUser(int adminID, String username, String passwordHash, String fullName, boolean isActive) {
        this.adminID = adminID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.isActive = isActive;
    }

    public int getAdminID() { return adminID; }
    public void setAdminID(int adminID) { this.adminID = adminID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}