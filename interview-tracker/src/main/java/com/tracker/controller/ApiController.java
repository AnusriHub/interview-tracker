package com.tracker.controller;

import com.tracker.dto.DashboardStats;
import com.tracker.model.ApplicationStatus;
import com.tracker.model.JobApplication;
import com.tracker.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ApplicationService service;

    @GetMapping("/applications")
    public List<JobApplication> getApplications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {

        return service.filterApplications(status, search);
    }

    @GetMapping("/stats")
    public DashboardStats getStats() {
        return service.getDashboardStats();
    }

    @PatchMapping("/applications/{id}/status")
    public ResponseEntity<Map<String, Object>> quickStatusUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        var application = service.getById(id);

        if (application.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Application not found");

            return ResponseEntity.status(404).body(response);
        }

        try {
            ApplicationStatus newStatus =
                    ApplicationStatus.valueOf(body.get("status"));

            var dto = service.toDto(application.get());
            dto.setStatus(newStatus);

            service.update(id, dto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("status", newStatus.getDisplayName());
            response.put("cssClass", newStatus.getCssClass());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}