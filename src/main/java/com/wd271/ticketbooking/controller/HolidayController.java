package com.wd271.ticketbooking.controller;

import com.wd271.ticketbooking.model.HolidayPackage;
import com.wd271.ticketbooking.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/holidays")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    // CREATE
    @PostMapping("/add")
    public String add(@RequestBody Map<String, String> body) {
        return holidayService.addPackage(
                body.get("name"),
                body.get("destination"),
                body.get("date"),
                Double.parseDouble(
                        body.getOrDefault("price", "0")),
                Integer.parseInt(
                        body.getOrDefault("availableSeats", "0")),
                body.get("description"),
                body.get("packageType"),
                Integer.parseInt(
                        body.getOrDefault("numberOfDays", "1"))
        );
    }

    // READ — all packages
    @GetMapping("/all")
    public List<HolidayPackage> getAll() {
        return holidayService.getAllPackages();
    }

    // READ — search by destination
    @GetMapping("/search/{destination}")
    public List<HolidayPackage> search(
            @PathVariable String destination) {
        return holidayService.searchByDestination(destination);
    }

    // READ — get one by ID
    @GetMapping("/{packageId}")
    public HolidayPackage getById(
            @PathVariable String packageId) {
        return holidayService.findById(packageId);
    }

    // UPDATE
    @PutMapping("/update")
    public String update(
            @RequestBody Map<String, String> body) {
        return holidayService.updatePackage(
                body.get("packageId"),
                body.get("name"),
                body.get("destination"),
                body.get("date"),
                Double.parseDouble(
                        body.getOrDefault("price", "0")),
                Integer.parseInt(
                        body.getOrDefault("availableSeats", "0")),
                body.get("description")
        );
    }

    // DELETE
    @DeleteMapping("/delete/{packageId}")
    public String delete(@PathVariable String packageId) {
        return holidayService.deletePackage(packageId);
    }
}