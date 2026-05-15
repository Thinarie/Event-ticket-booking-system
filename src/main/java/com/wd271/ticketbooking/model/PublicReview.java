package com.wd271.ticketbooking.model;

// INHERITANCE — extends Review
// POLYMORPHISM — displayReview() shows public format
public class PublicReview extends Review {

    public PublicReview(String reviewId, String userId,
                        String eventId, String eventName,
                        int rating, String comment,
                        String date) {
        super(reviewId, userId, eventId, eventName,
                rating, comment, date);
    }

    // POLYMORPHISM — public display format
    @Override
    public String displayReview() {
        return "[PUBLIC] " + getUserId() +
                " rated " + getRating() + "/5 — " +
                getComment();
    }

    @Override
    public String getReviewType() {
        return "PUBLIC";
    }
}