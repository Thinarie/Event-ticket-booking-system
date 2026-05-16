package com.wd271.ticketbooking.service;

import com.wd271.ticketbooking.model.DayTrip;
import com.wd271.ticketbooking.model.HolidayPackage;
import com.wd271.ticketbooking.model.MultiDayTrip;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private static final String FILE_PATH =
            "src/main/resources/data/holidays.txt";

    // ── CREATE ───────────────────────────────────────────────
    public String addPackage(String name, String destination,
                             String date, double price,
                             int availableSeats,
                             String description,
                             String packageType,
                             int numberOfDays) {
        // input validation
        if (name == null || name.trim().isEmpty()) {
            return "Package name cannot be empty!";
        }
        if (destination == null || destination.trim().isEmpty()) {
            return "Destination cannot be empty!";
        }
        if (price <= 0) {
            return "Price must be greater than zero!";
        }
        if (availableSeats <= 0) {
            return "Available seats must be greater than zero!";
        }

        try {
            String id = "H" + getNextId();
            HolidayPackage newPackage;

            // POLYMORPHISM — create correct subclass
            if (packageType != null &&
                    packageType.equals("MULTIDAY")) {
                newPackage = new MultiDayTrip(
                        id, name, destination, date,
                        price, availableSeats,
                        description, numberOfDays
                );
            } else {
                newPackage = new DayTrip(
                        id, name, destination, date,
                        price, availableSeats, description
                );
            }

            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FILE_PATH, true)
            );
            bw.write(newPackage.toFileString());
            bw.newLine();
            bw.close();

            return "Holiday package added successfully!";

        } catch (IOException e) {
            return "Error saving package: " + e.getMessage();
        }
    }

    // ── READ — get all packages ──────────────────────────────
    public List<HolidayPackage> getAllPackages() {
        List<HolidayPackage> list = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return list;

            BufferedReader br = new BufferedReader(
                    new FileReader(FILE_PATH)
            );
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(parseLine(line.trim()));
                }
            }
            br.close();

        } catch (IOException e) {
            System.out.println("Read error: " + e.getMessage());
        }
        return list;
    }

    // ── READ — search by destination ─────────────────────────
    public List<HolidayPackage> searchByDestination(
            String destination) {
        return getAllPackages()
                .stream()
                .filter(h -> h.getDestination()
                        .toLowerCase()
                        .contains(destination.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ── READ — find by ID ────────────────────────────────────
    public HolidayPackage findById(String packageId) {
        return getAllPackages()
                .stream()
                .filter(h -> h.getPackageId().equals(packageId))
                .findFirst()
                .orElse(null);
    }

    // ── UPDATE ───────────────────────────────────────────────
    public String updatePackage(String packageId,
                                String newName,
                                String newDestination,
                                String newDate,
                                double newPrice,
                                int newSeats,
                                String newDescription) {
        List<HolidayPackage> list = getAllPackages();
        boolean found             = false;

        for (HolidayPackage h : list) {
            if (h.getPackageId().equals(packageId)) {
                if (newName != null &&
                        !newName.trim().isEmpty()) {
                    h.setName(newName);
                }
                if (newDestination != null &&
                        !newDestination.trim().isEmpty()) {
                    h.setDestination(newDestination);
                }
                if (newDate != null &&
                        !newDate.trim().isEmpty()) {
                    h.setDate(newDate);
                }
                if (newPrice > 0)  h.setPrice(newPrice);
                if (newSeats > 0)  h.setAvailableSeats(newSeats);
                if (newDescription != null &&
                        !newDescription.trim().isEmpty()) {
                    h.setDescription(newDescription);
                }
                found = true;
                break;
            }
        }

        if (!found) return "Package not found!";
        rewriteFile(list);
        return "Package updated successfully!";
    }

    // ── DELETE ───────────────────────────────────────────────
    public String deletePackage(String packageId) {
        List<HolidayPackage> list = getAllPackages();
        boolean removed           = list.removeIf(
                h -> h.getPackageId().equals(packageId)
        );

        if (!removed) return "Package not found!";
        rewriteFile(list);
        return "Package deleted successfully!";
    }

    // ── HELPER: generate sequential ID ──────────────────────
    private int getNextId() {
        List<HolidayPackage> all = getAllPackages();
        if (all.isEmpty()) return 1;

        int maxId = 0;
        for (HolidayPackage h : all) {
            try {
                int currentId = Integer.parseInt(
                        h.getPackageId().replace("H", "")
                );
                if (currentId > maxId) maxId = currentId;
            } catch (NumberFormatException e) {
                // skip
            }
        }
        return maxId + 1;
    }

    // ── HELPER: convert txt line → HolidayPackage object ────
    private HolidayPackage parseLine(String line) {
        String[] p    = line.split(",");
        // format: packageId,name,destination,date,
        //         price,seats,description,type
        double price  = Double.parseDouble(p[4]);
        int    seats  = Integer.parseInt(p[5]);

        if (p[7].equals("MULTIDAY")) {
            return new MultiDayTrip(
                    p[0], p[1], p[2], p[3],
                    price, seats, p[6], 2
            );
        }
        return new DayTrip(
                p[0], p[1], p[2], p[3],
                price, seats, p[6]
        );
    }

    // ── HELPER: overwrite entire file ───────────────────────
    private void rewriteFile(List<HolidayPackage> list) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FILE_PATH, false)
            );
            for (HolidayPackage h : list) {
                bw.write(h.toFileString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Write error: " + e.getMessage());
        }
    }
}