package com.wd271.ticketbooking.model;


public class AdminUser extends user {

    public AdminUser(String userId, String username,
                     String password, String email) {
        super(userId, username, password, email);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}