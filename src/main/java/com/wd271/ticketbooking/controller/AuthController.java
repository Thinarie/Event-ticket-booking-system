package com.wd271.ticketbooking.controller;


import com.wd271.ticketbooking.model.user;
import com.wd271.ticketbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private UserService userService;

    // CREATE
    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> body) {
        return userService.registerUser(
                body.get("username"),
                body.get("password"),
                body.get("email")

        );
    }
    // ADD this to AuthController.java
// READ — search users by username
    // READ — search users by username
    @GetMapping("/search")
    public user search(@RequestParam String keyword) {
        return userService.findByUsername(keyword);
    }

    // READ all
    @GetMapping("/all")
    public List<user> getAll() {
        return userService.getAllUsers();
    }

    // READ one - login
//git commit -m "Add CrossOrigin annotation to UserController for frontend API access"
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> body) {
        return userService.login(
                body.get("username"),
                body.get("password")
        );
    }
    // UPDATE
    @PutMapping("/update")
    public String update(@RequestBody Map<String, String> body) {
        return userService.updateUser(
                body.get("username"),
                body.get("email"),
                body.get("password")
        );
    }

    // DELETE
    @DeleteMapping("/delete/{username}")
    public String delete(@PathVariable String username) {
        return userService.deleteUser(username);
    }
}


