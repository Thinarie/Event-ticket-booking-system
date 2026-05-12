package com.wd271.ticketbooking.model;

// Abstract base class — ABSTRACTION and ENCAPSULATION
public abstract class Review {

    // ENCAPSULATION — private fields
    private String reviewId;
    private String userId;
    private String eventId;
    private String eventName;
    private int    rating;
    private String comment;
    private String date;

    public Review(String reviewId, String userId,
                  String eventId, String eventName,
                  int rating, String comment, String date) {
        this.reviewId  = reviewId;
        this.userId    = userId;
        this.eventId   = eventId;
        this.eventName = eventName;
        this.rating    = rating;
        this.comment   = comment;
        this.date      = date;
    }

    // ABSTRACTION — subclasses must define display format
    public abstract String displayReview();

    // ABSTRACTION — subclasses must define their type
    public abstract String getReviewType();

    // converts to one line for saving in reviews.txt
    public String toFileString() {
        return String.join(",",
                reviewId, userId, eventId, eventName,
                String.valueOf(rating), comment,
                date, getReviewType()
        );
    }

    // METHOD OVERRIDING
    @Override
    public String toString() {
        return "Review{id=" + reviewId +
                ", user=" + userId +
                ", rating=" + rating +
                ", type=" + getReviewType() + "}";
    }

    // GETTERS
    public String getReviewId()  { return reviewId; }
    public String getUserId()    { return userId; }
    public String getEventId()   { return eventId; }
    public String getEventName() { return eventName; }
    public int    getRating()    { return rating; }
    public String getComment()   { return comment; }
    public String getDate()      { return date; }

    // SETTERS
    public void setRating(int rating)      { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
}