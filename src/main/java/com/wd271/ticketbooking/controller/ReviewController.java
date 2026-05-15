package com.wd271.ticketbooking.controller;

import com.wd271.ticketbooking.model.Review;
import com.wd271.ticketbooking.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // CREATE
    @PostMapping("/add")
    public String add(@RequestBody Map<String, String> body) {
        return reviewService.addReview(
                body.get("userId"),
                body.get("eventId"),
                body.get("eventName"),
                Integer.parseInt(
                        body.getOrDefault("rating", "1")),
                body.get("comment"),
                body.get("reviewType")
        );
    }

    // READ — all reviews
    @GetMapping("/all")
    public List<Review> getAll() {
        return reviewService.getAllReviews();
    }

    // READ — by event
    @GetMapping("/event/{eventId}")
    public List<Review> getByEvent(
            @PathVariable String eventId) {
        return reviewService.getReviewsByEvent(eventId);
    }

    // READ — by user
    @GetMapping("/user/{userId}")
    public List<Review> getByUser(
            @PathVariable String userId) {
        return reviewService.getReviewsByUser(userId);
    }

    // READ — single review
    @GetMapping("/{reviewId}")
    public Review getById(@PathVariable String reviewId) {
        return reviewService.findById(reviewId);
    }

    // READ — average rating
    @GetMapping("/rating/{eventId}")
    public String getAvgRating(
            @PathVariable String eventId) {
        double avg = reviewService.getAverageRating(eventId);
        return "Average rating for " + eventId +
                ": " + avg + "/5";
    }

    // UPDATE
    @PutMapping("/update")
    public String update(
            @RequestBody Map<String, String> body) {
        return reviewService.updateReview(
                body.get("reviewId"),
                Integer.parseInt(
                        body.getOrDefault("rating", "1")),
                body.get("comment")
        );
    }

    // DELETE
    @DeleteMapping("/delete/{reviewId}")
    public String delete(@PathVariable String reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}