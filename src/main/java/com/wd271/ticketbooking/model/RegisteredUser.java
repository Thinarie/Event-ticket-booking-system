package com.wd271.ticketbooking.model;


public class RegisteredUser extends user {

    public RegisteredUser(String userId, String username,
                          String password, String email) {
        super(userId, username, password, email);
    }

    @Override
    public String getRole() {
        return "REGISTERED";
    }
}