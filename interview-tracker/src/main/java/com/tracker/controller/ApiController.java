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
        return (ResponseEntity<Map<String, Object>>) service.getById(id).map(app -> {
            try {
                ApplicationStatus newStatus = ApplicationStatus.valueOf(body.get("status"));
                var dto = service.toDto(app);
                dto.setStatus(newStatus);
                service.update(id, dto);
                Map<String, Object> resp = new HashMap<>();
                resp.put("success", true);
                resp.put("status", newStatus.getDisplayName());
                resp.put("cssClass", newStatus.getCssClass());
                return ResponseEntity.ok(resp);
            } catch (Exception e) {
                return ResponseEntity.<Map<String, Object>>badRequest().build();
            }
        }).orElseGet(() -> {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", false);
            resp.put("message", "Application not found");
            return ResponseEntity.status(404).body(resp);
        });
    }
}
