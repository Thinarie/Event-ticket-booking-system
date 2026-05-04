package com.wd271.ticketbooking.model;

// Abstract base class — ABSTRACTION and ENCAPSULATION
public abstract class HolidayPackage {

    // ENCAPSULATION — private fields
    private String packageId;
    private String name;
    private String destination;
    private String date;
    private double price;
    private int availableSeats;
    private String description;

    public HolidayPackage(String packageId, String name,
                          String destination, String date,
                          double price, int availableSeats,
                          String description) {
        this.packageId      = packageId;
        this.name           = name;
        this.destination    = destination;
        this.date           = date;
        this.price          = price;
        this.availableSeats = availableSeats;
        this.description    = description;
    }

    // ABSTRACTION — subclasses must define type
    public abstract String getPackageType();

    // ABSTRACTION — subclasses must define price calculation
    public abstract double calculateTotalPrice(int numberOfPeople);

    // converts to one line for saving in holidays.txt
    public String toFileString() {
        return String.join(",",
                packageId, name, destination, date,
                String.valueOf(price),
                String.valueOf(availableSeats),
                description, getPackageType()
        );
    }

    // METHOD OVERRIDING
    @Override
    public String toString() {
        return "HolidayPackage{id=" + packageId +
                ", name=" + name +
                ", type=" + getPackageType() + "}";
    }

    // GETTERS
    public String getPackageId()     { return packageId; }
    public String getName()          { return name; }
    public String getDestination()   { return destination; }
    public String getDate()          { return date; }
    public double getPrice()         { return price; }
    public int getAvailableSeats()   { return availableSeats; }
    public String getDescription()   { return description; }

    // SETTERS
    public void setName(String name)                  { this.name = name; }
    public void setDestination(String destination)    { this.destination = destination; }
    public void setDate(String date)                  { this.date = date; }
    public void setPrice(double price)                { this.price = price; }
    public void setAvailableSeats(int seats)          { this.availableSeats = seats; }
    public void setDescription(String description)    { this.description = description; }
}