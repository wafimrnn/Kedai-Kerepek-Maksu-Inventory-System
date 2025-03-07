package com.model;

public class User {
    private int id;
    private String name;
    private String role;
    private String phone;
    private String address;
    private String userRole;
    private String accStatus; // Added account status field

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name != null ? name : "N/A"; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role != null ? role : "N/A"; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone != null ? phone : "N/A"; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address != null ? address : "N/A"; }
    public void setAddress(String address) { this.address = address; }

    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAccStatus() { // Added getter for account status
        return accStatus != null ? accStatus : "ACTIVE";
    }
    public void setAccStatus(String accStatus) { // Added setter for account status
        this.accStatus = accStatus;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", role='" + role + '\'' +
               ", phone='" + phone + '\'' +
               ", address='" + address + '\'' +
               ", userRole='" + userRole + '\'' +
               ", accStatus='" + accStatus + '\'' +
               '}';
    }
}