package com.example.ticketbooking.model;


public abstract class Payment {

    // Private fields (Encapsulation) 
    private String paymentId;
    private String ticketId;
    private double amount;
    private String paymentDate;
    private String status;   // PENDING | COMPLETED | FAILED

    // Constructors 
    public Payment() {}

    public Payment(String paymentId, String ticketId, double amount,
                   String paymentDate, String status) {
        this.paymentId   = paymentId;
        this.ticketId    = ticketId;
        this.amount      = amount;
        this.paymentDate = paymentDate;
        this.status      = status;
    }

    // Abstract methods (Abstraction + Polymorphism) 

  
    public abstract String processPayment();

    
    public abstract String getPaymentType();

   
    public abstract String toFileString();

    // Getters & Setters (Encapsulation) 
    public String getPaymentId()              { return paymentId; }
    public void   setPaymentId(String id)     { this.paymentId = id; }

    public String getTicketId()               { return ticketId; }
    public void   setTicketId(String tid)     { this.ticketId = tid; }

    public double getAmount()                 { return amount; }
    public void   setAmount(double amount)    { this.amount = amount; }

    public String getPaymentDate()            { return paymentDate; }
    public void   setPaymentDate(String date) { this.paymentDate = date; }

    public String getStatus()                 { return status; }
    public void   setStatus(String status)    { this.status = status; }

    @Override
    public String toString() {
        return "Payment{id='" + paymentId + "', ticket='" + ticketId
                + "', amount=" + amount + ", date='" + paymentDate
                + "', status='" + status + "'}";
    }
}
