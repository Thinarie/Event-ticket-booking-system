package com.wd271.ticketbooking.model;

// INHERITANCE — extends Review
// POLYMORPHISM — displayReview() shows verified badge
public class VerifiedReview extends Review {

    public VerifiedReview(String reviewId, String userId,
                          String eventId, String eventName,
                          int rating, String comment,
                          String date) {
        super(reviewId, userId, eventId, eventName,
                rating, comment, date);
    }

    // POLYMORPHISM — verified display format with badge
    @Override
    public String displayReview() {
        return "✅ [VERIFIED] " + getUserId() +
                " rated " + getRating() + "/5 — " +
                getComment();
    }

    @Override
    public String getReviewType() {
        return "VERIFIED";
    }
}