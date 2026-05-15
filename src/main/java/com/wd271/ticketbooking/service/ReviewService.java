package com.wd271.ticketbooking.service;

import com.wd271.ticketbooking.model.PublicReview;
import com.wd271.ticketbooking.model.Review;
import com.wd271.ticketbooking.model.VerifiedReview;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final String FILE_PATH =
            "src/main/resources/data/reviews.txt";

    // ── CREATE ───────────────────────────────────────────────
    public String addReview(String userId, String eventId,
                            String eventName, int rating,
                            String comment,
                            String reviewType) {
        // input validation
        if (userId == null || userId.trim().isEmpty()) {
            return "User ID cannot be empty!";
        }
        if (eventId == null || eventId.trim().isEmpty()) {
            return "Event ID cannot be empty!";
        }
        if (rating < 1 || rating > 5) {
            return "Rating must be between 1 and 5!";
        }
        if (comment == null || comment.trim().isEmpty()) {
            return "Comment cannot be empty!";
        }

        // check user already reviewed this event
        boolean alreadyReviewed = getAllReviews()
                .stream()
                .anyMatch(r -> r.getUserId().equals(userId)
                        && r.getEventId().equals(eventId));

        if (alreadyReviewed) {
            return "You already reviewed this event!";
        }

        try {
            String id   = "R" + getNextId();
            String date = java.time.LocalDate.now().toString();
            Review newReview;

            // POLYMORPHISM — create correct subclass
            if (reviewType != null &&
                    reviewType.equals("VERIFIED")) {
                newReview = new VerifiedReview(
                        id, userId, eventId, eventName,
                        rating, comment, date
                );
            } else {
                newReview = new PublicReview(
                        id, userId, eventId, eventName,
                        rating, comment, date
                );
            }

            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FILE_PATH, true)
            );
            bw.write(newReview.toFileString());
            bw.newLine();
            bw.close();

            return "Review submitted successfully!";

        } catch (IOException e) {
            return "Error saving review: " + e.getMessage();
        }
    }

    // ── READ — get all reviews ───────────────────────────────
    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
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

    // ── READ — get reviews by event ──────────────────────────
    public List<Review> getReviewsByEvent(String eventId) {
        return getAllReviews()
                .stream()
                .filter(r -> r.getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    // ── READ — get reviews by user ───────────────────────────
    public List<Review> getReviewsByUser(String userId) {
        return getAllReviews()
                .stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // ── READ — find by ID ────────────────────────────────────
    public Review findById(String reviewId) {
        return getAllReviews()
                .stream()
                .filter(r -> r.getReviewId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    // ── READ — average rating for event ─────────────────────
    public double getAverageRating(String eventId) {
        List<Review> eventReviews = getReviewsByEvent(eventId);
        if (eventReviews.isEmpty()) return 0;

        return eventReviews
                .stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    // ── UPDATE ───────────────────────────────────────────────
    public String updateReview(String reviewId,
                               int newRating,
                               String newComment) {
        List<Review> list = getAllReviews();
        boolean found     = false;

        for (Review r : list) {
            if (r.getReviewId().equals(reviewId)) {
                if (newRating >= 1 && newRating <= 5) {
                    r.setRating(newRating);
                }
                if (newComment != null &&
                        !newComment.trim().isEmpty()) {
                    r.setComment(newComment);
                }
                found = true;
                break;
            }
        }

        if (!found) return "Review not found!";
        rewriteFile(list);
        return "Review updated successfully!";
    }

    // ── DELETE ───────────────────────────────────────────────
    public String deleteReview(String reviewId) {
        List<Review> list = getAllReviews();
        boolean removed   = list.removeIf(
                r -> r.getReviewId().equals(reviewId)
        );

        if (!removed) return "Review not found!";
        rewriteFile(list);
        return "Review deleted successfully!";
    }

    // ── HELPER: generate sequential ID ──────────────────────
    private int getNextId() {
        List<Review> all = getAllReviews();
        if (all.isEmpty()) return 1;

        int maxId = 0;
        for (Review r : all) {
            try {
                int currentId = Integer.parseInt(
                        r.getReviewId().replace("R", "")
                );
                if (currentId > maxId) maxId = currentId;
            } catch (NumberFormatException e) {
                // skip
            }
        }
        return maxId + 1;
    }

    // ── HELPER: convert txt line → Review object ─────────────
    private Review parseLine(String line) {
        String[] p = line.split(",");
        // format: reviewId,userId,eventId,eventName,
        //         rating,comment,date,type
        int rating = Integer.parseInt(p[4]);

        if (p[7].equals("VERIFIED")) {
            return new VerifiedReview(
                    p[0], p[1], p[2], p[3],
                    rating, p[5], p[6]
            );
        }
        return new PublicReview(
                p[0], p[1], p[2], p[3],
                rating, p[5], p[6]
        );
    }

    // ── HELPER: overwrite entire file ───────────────────────
    private void rewriteFile(List<Review> list) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FILE_PATH, false)
            );
            for (Review r : list) {
                bw.write(r.toFileString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Write error: " + e.getMessage());
        }
    }
}