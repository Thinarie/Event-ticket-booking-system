package com.example.ticketbooking.controller;

import com.example.ticketbooking.model.CardPayment;
import com.example.ticketbooking.model.CashPayment;
import com.example.ticketbooking.model.Payment;
import com.example.ticketbooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller – exposes HTTP endpoints for Payment CRUD operations.
 * The static HTML pages in /resources/static call these endpoints via fetch().
 *
 * Base URL: /api/payments
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    
    // CREATE   POST /api/payments/create
   
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String paymentId = paymentService.generatePaymentId();
            String type      = request.get("paymentType");       // "CARD" or "CASH"
            String ticketId  = request.get("ticketId");
            double amount    = Double.parseDouble(request.get("amount"));
            String date      = request.get("paymentDate");
            String status    = "COMPLETED";

            Payment payment;

            if ("CARD".equalsIgnoreCase(type)) {
                String cardHolder = request.get("cardHolderName");
                String rawCard    = request.get("cardNumber");
                // Mask the card number – only show last 4 digits
                String masked = "**** **** **** "
                        + rawCard.substring(Math.max(0, rawCard.length() - 4));
                payment = new CardPayment(paymentId, ticketId, amount,
                                          date, status, cardHolder, masked);

            } else {
                // CASH payment
                String receivedBy = request.get("receivedBy");
                payment = new CashPayment(paymentId, ticketId, amount,
                                          date, status, receivedBy);
            }

            // Polymorphic call – each subclass processes differently
            String processMessage = payment.processPayment();

            boolean saved = paymentService.addPayment(payment);

            response.put("success", saved);
            response.put("paymentId", paymentId);
            response.put("message", processMessage);
            response.put("paymentType", type);
            response.put("amount", amount);
            response.put("ticketId", ticketId);
            response.put("date", date);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating payment: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    
    // READ ALL   GET /api/payments/all
    
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Payment p : payments) {
            result.add(paymentToMap(p));
        }
        return ResponseEntity.ok(result);
    }

   
    // READ BY ID   GET /api/payments/{id}
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPaymentById(@PathVariable String id) {
        Payment p = paymentService.getPaymentById(id);
        if (p == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Payment not found with ID: " + id);
            return ResponseEntity.status(404).body(err);
        }
        return ResponseEntity.ok(paymentToMap(p));
    }

    
    // UPDATE   PUT /api/payments/update
    
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePayment(
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new LinkedHashMap<>();
        try {
            String paymentId = request.get("paymentId");
            Payment existing = paymentService.getPaymentById(paymentId);

            if (existing == null) {
                response.put("success", false);
                response.put("message", "Payment not found: " + paymentId);
                return ResponseEntity.ok(response);
            }

            // Update common fields
            existing.setAmount(Double.parseDouble(request.get("amount")));
            existing.setStatus(request.get("status"));
            existing.setPaymentDate(request.get("paymentDate"));

            // Update type-specific fields
            if (existing instanceof CardPayment) {
                ((CardPayment) existing).setCardHolderName(request.get("cardHolderName"));
            } else if (existing instanceof CashPayment) {
                ((CashPayment) existing).setReceivedBy(request.get("receivedBy"));
            }

            boolean updated = paymentService.updatePayment(existing);
            response.put("success", updated);
            response.put("message", updated
                    ? "Payment updated successfully."
                    : "Update failed. Please try again.");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating payment: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    
    // DELETE   DELETE /api/payments/delete/{id}
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deletePayment(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        boolean deleted = paymentService.deletePayment(id);
        response.put("success", deleted);
        response.put("message", deleted
                ? "Payment " + id + " deleted successfully."
                : "Payment not found: " + id);
        return ResponseEntity.ok(response);
    }

    
    // PRIVATE HELPER – convert a Payment object to a JSON-friendly Map
    
    private Map<String, Object> paymentToMap(Payment p) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("paymentId",   p.getPaymentId());
        map.put("paymentType", p.getPaymentType());
        map.put("ticketId",    p.getTicketId());
        map.put("amount",      p.getAmount());
        map.put("paymentDate", p.getPaymentDate());
        map.put("status",      p.getStatus());

        // Add type-specific fields
        if (p instanceof CardPayment cp) {
            map.put("cardHolderName",  cp.getCardHolderName());
            map.put("maskedCardNumber", cp.getMaskedCardNumber());
        } else if (p instanceof CashPayment cash) {
            map.put("receivedBy", cash.getReceivedBy());
        }
        return map;
    }
}
