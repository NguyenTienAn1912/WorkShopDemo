package com.example.ratingservice.dto;

public class RatingAverageResponse {
    private Long itemId;
    private String itemName;
    private Double averageRating;
    private Long totalRatings;

    // Constructors
    public RatingAverageResponse() {}

    public RatingAverageResponse(Long itemId, String itemName, Double averageRating, Long totalRatings) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.averageRating = averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0;
        this.totalRatings = totalRatings;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0;
    }

    public Long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Long totalRatings) {
        this.totalRatings = totalRatings;
    }
}