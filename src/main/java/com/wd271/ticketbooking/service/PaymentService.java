package com.example.ticketbooking.service;

import com.example.ticketbooking.model.CardPayment;
import com.example.ticketbooking.model.CashPayment;
import com.example.ticketbooking.model.Payment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer that handles all CRUD operations for payments.
 * Data is persisted to a plain text file (payments.txt) using
 * Java file I/O – no database required.
 */
@Service
public class PaymentService {

    // Path to the data file
    private static final String FILE_PATH = "src/main/resources/data/payments.txt";

    
    // CREATE – append a new payment to the file
    
    public boolean addPayment(Payment payment) {
        ensureFileExists();
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_PATH, true))) {  // true = append
            writer.write(payment.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    // READ ALL – read every line and convert to Payment objects
    
    public List<Payment> getAllPayments() {
        ensureFileExists();
        List<Payment> payments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Payment p = parsePayment(line);
                    if (p != null) {
                        payments.add(p);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payments;
    }

    
    // READ BY ID – search through all payments for a matching ID
    
    public Payment getPaymentById(String paymentId) {
        return getAllPayments().stream()
                .filter(p -> p.getPaymentId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }

    
    // UPDATE – find by ID, replace, rewrite the whole file
    
    public boolean updatePayment(Payment updated) {
        List<Payment> all = getAllPayments();
        boolean found = false;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPaymentId().equals(updated.getPaymentId())) {
                all.set(i, updated);
                found = true;
                break;
            }
        }

        if (found) {
            writeAllToFile(all);
        }
        return found;
    }

    
    // DELETE – remove by ID, rewrite the whole file
    
    public boolean deletePayment(String paymentId) {
        List<Payment> all = getAllPayments();
        boolean removed = all.removeIf(p -> p.getPaymentId().equals(paymentId));
        if (removed) {
            writeAllToFile(all);
        }
        return removed;
    }

    
    // HELPER – generate a unique ID based on current timestamp
    
    public String generatePaymentId() {
        return "PAY" + System.currentTimeMillis();
    }

    
    // PRIVATE HELPERS
    

    /** Overwrite the file with a fresh list of payments */
    private void writeAllToFile(List<Payment> payments) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_PATH, false))) {  // false = overwrite
            for (Payment p : payments) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse a CSV line back into a Payment object.
     * Line format: paymentId,TYPE,ticketId,amount,date,status,extra1,extra2
     */
    private Payment parsePayment(String line) {
        String[] parts = line.split(",", -1);  // -1 keeps trailing empty strings
        if (parts.length < 8) return null;

        String paymentId = parts[0].trim();
        String type      = parts[1].trim();
        String ticketId  = parts[2].trim();
        double amount    = Double.parseDouble(parts[3].trim());
        String date      = parts[4].trim();
        String status    = parts[5].trim();
        String extra1    = parts[6].trim();   // cardHolderName OR receivedBy
        String extra2    = parts[7].trim();   // maskedCardNumber OR empty

        if ("CARD".equalsIgnoreCase(type)) {
            return new CardPayment(paymentId, ticketId, amount, date, status, extra1, extra2);
        } else if ("CASH".equalsIgnoreCase(type)) {
            return new CashPayment(paymentId, ticketId, amount, date, status, extra1);
        }
        return null;
    }

    /** Create the file (and parent directories) if it does not yet exist */
    private void ensureFileExists() {
        try {
            Path path = Paths.get(FILE_PATH);
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
