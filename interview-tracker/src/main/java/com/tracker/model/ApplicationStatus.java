package com.tracker.model;

public enum ApplicationStatus {
    APPLIED("Applied", "status-applied"),
    OA("Online Assessment", "status-oa"),
    INTERVIEW("Interview", "status-interview"),
    OFFER("Offer", "status-offer"),
    REJECTED("Rejected", "status-rejected"),
    WITHDRAWN("Withdrawn", "status-withdrawn");

    private final String displayName;
    private final String cssClass;

    ApplicationStatus(String displayName, String cssClass) {
        this.displayName = displayName;
        this.cssClass = cssClass;
    }

    public String getDisplayName() { return displayName; }
    public String getCssClass() { return cssClass; }
}
