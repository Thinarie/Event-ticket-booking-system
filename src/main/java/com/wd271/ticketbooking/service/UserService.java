package com.wd271.ticketbooking.service;

import com.wd271.ticketbooking.model.AdminUser;
import com.wd271.ticketbooking.model.RegisteredUser;
import com.wd271.ticketbooking.model.user;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

@Service
public class UserService {

    private static final String FILE_PATH = "src/main/resources/data/users.txt";

    // Convert one line from file into a user object
    private user parseLine(String line) {
        String[] parts = line.split(",");

        String id = parts[0];
        String name = parts[1];
        String username = parts[2];
        String password = parts[3];
        String role = parts[4];

        if (role.equals("ADMIN")) {
            return new AdminUser(id, name, username, password);
        } else {
            return new RegisteredUser(id, name, username, password);
        }
    }

    public List<user> getAllUsers() {
        List<user> users = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return users;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isEmpty()) {
                    users.add(parseLine(line)); // FIXED
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        return users;
    }public user findUserByUsername(String username) {
        List<user> users = getAllUsers();

        for (user u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
    // READ - find one
        public user findByUsername(String username) {
            for (user u : getAllUsers()) {
                if (u.getUsername().equals(username)) return u;
            }
            return null;
        }
    //CREATE
    public String registerUser(String username, String password, String email) {

        if (findByUsername(username) != null) {
            return "Username already taken!";
        }

        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {

            String id = "U" + System.currentTimeMillis();
            user newUser = new RegisteredUser(id, username, password, email);

            fw.write(newUser.toFileString() + "\n");

            return "Registration successful!";

        } catch (IOException e) {
            return "Error writing file";
        }
    }

}