package com.wd271.ticketbooking.service;

import com.wd271.ticketbooking.model.AdminUser;
import com.wd271.ticketbooking.model.RegisteredUser;
import com.wd271.ticketbooking.model.user;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

    // Rewrite the whole file with updated users
    private void rewriteFile(List<user> users) {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);

            for (user u : users) {
                writer.write(u.toFileString());
                writer.write("\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }
}