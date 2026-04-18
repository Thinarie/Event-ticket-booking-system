package com.wd271.ticketbooking.model;


public abstract class user {

    private String userId;
    private String username;
    private String password;
    private String email;

    public user(String userId, String username,
                String password, String email) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.email    = email;
    }

    // abstract method — subclasses must define their role
    public abstract String getRole();

    // converts user to one line for saving in users.txt
    public String toFileString() {
        return userId + "," + username + "," +
                password + "," + email + "," + getRole();
    }

    // getters
    public String getUserId()   { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }

    // setters
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email)       { this.email = email; }
}