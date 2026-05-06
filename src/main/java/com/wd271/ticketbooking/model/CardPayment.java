package com.example.ticketbooking.model;

/**
 * Represents a card (credit/debit) payment.
 * Demonstrates INHERITANCE – extends the abstract Payment class.
 */
public class CardPayment extends Payment 
{

    //  Extra fields specific to card payments 
    private String cardHolderName;
    private String maskedCardNumber;   // e.g. "**** **** **** 1234"

    //  Constructors 
    public CardPayment() 
    {
        super();
    }

    public CardPayment(String paymentId, String ticketId, double amount,
                       String paymentDate, String status,
                       String cardHolderName, String maskedCardNumber) 
                       {
        super(paymentId, ticketId, amount, paymentDate, status);
        this.cardHolderName    = cardHolderName;
        this.maskedCardNumber  = maskedCardNumber;
    }

    //  Overridden abstract methods (Polymorphism) 

    @Override
    public String processPayment() 
    {
        // Card-specific processing logic
        return "Card payment of LKR " + getAmount()
                + " processed for " + cardHolderName
                + " (" + maskedCardNumber + ").";
    }

    @Override
    public String getPaymentType() 
    {
        return "CARD";
    }

    /**
     * CSV format:
     * paymentId,CARD,ticketId,amount,paymentDate,status,cardHolderName,maskedCardNumber
     */
    @Override
    public String toFileString() 
    {
        return getPaymentId()   + ","
             + getPaymentType() + ","
             + getTicketId()    + ","
             + getAmount()      + ","
             + getPaymentDate() + ","
             + getStatus()      + ","
             + cardHolderName   + ","
             + maskedCardNumber;
    }

    //  Getters & Setters 
    public String getCardHolderName()                      { return cardHolderName; }
    public void   setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getMaskedCardNumber()                        { return maskedCardNumber; }
    public void   setMaskedCardNumber(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }
}
