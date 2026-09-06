package com.tracker.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private long totalApplications;
    private long applied;
    private long onlineAssessment;
    private long interview;
    private long offer;
    private long rejected;
    private long withdrawn;

    public double getSuccessRate() {
        if (totalApplications == 0) return 0;
        return Math.round(((double) offer / totalApplications) * 100 * 10.0) / 10.0;
    }

    public double getInterviewRate() {
        if (totalApplications == 0) return 0;
        return Math.round(((double) (interview + offer) / totalApplications) * 100 * 10.0) / 10.0;
    }
}
