package com.example.ticketbooking.model;

/**
 * Represents a cash payment.
 * Demonstrates INHERITANCE – extends the abstract Payment class.
 */
public class CashPayment extends Payment {

    //  Extra field specific to cash payments 
    private String receivedBy;   // Name of the staff member who received the cash

    //  Constructors 
    public CashPayment() {
        super();
    }

    public CashPayment(String paymentId, String ticketId, double amount,
                       String paymentDate, String status, String receivedBy) {
        super(paymentId, ticketId, amount, paymentDate, status);
        this.receivedBy = receivedBy;
    }

    //  Overridden abstract methods (Polymorphism) 

    @Override
    public String processPayment() {
        // Cash-specific processing logic
        return "Cash payment of LKR " + getAmount()
                + " received by staff member: " + receivedBy + ".";
    }

    @Override
    public String getPaymentType() {
        return "CASH";
    }

    /**
     * CSV format:
     * paymentId,CASH,ticketId,amount,paymentDate,status,receivedBy,
     * (trailing comma keeps column count consistent with CardPayment)
     */
    @Override
    public String toFileString() {
        return getPaymentId()   + ","
             + getPaymentType() + ","
             + getTicketId()    + ","
             + getAmount()      + ","
             + getPaymentDate() + ","
             + getStatus()      + ","
             + receivedBy       + ",";
    }

    //  Getters & Setters 
    public String getReceivedBy()               { return receivedBy; }
    public void   setReceivedBy(String name)    { this.receivedBy = name; }
}
