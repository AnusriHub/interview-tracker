package com.tracker.service;

import com.tracker.dto.ApplicationDTO;
import com.tracker.dto.DashboardStats;
import com.tracker.model.ApplicationStatus;
import com.tracker.model.JobApplication;
import com.tracker.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final JobApplicationRepository repository;

    public List<JobApplication> getAllApplications() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public List<JobApplication> filterApplications(String status, String search) {
        ApplicationStatus appStatus = null;
        if (status != null && !status.isEmpty()) {
            try { appStatus = ApplicationStatus.valueOf(status); } catch (Exception ignored) {}
        }
        String searchTerm = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return repository.findByFilters(appStatus, searchTerm);
    }

    public Optional<JobApplication> getById(Long id) {
        return repository.findById(id);
    }

    public JobApplication save(ApplicationDTO dto) {
        JobApplication app = new JobApplication();
        mapDtoToEntity(dto, app);
        return repository.save(app);
    }

    public JobApplication update(Long id, ApplicationDTO dto) {
        JobApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));
        mapDtoToEntity(dto, app);
        return repository.save(app);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public DashboardStats getDashboardStats() {
        long total = repository.count();
        return new DashboardStats(
                total,
                repository.countByStatus(ApplicationStatus.APPLIED),
                repository.countByStatus(ApplicationStatus.OA),
                repository.countByStatus(ApplicationStatus.INTERVIEW),
                repository.countByStatus(ApplicationStatus.OFFER),
                repository.countByStatus(ApplicationStatus.REJECTED),
                repository.countByStatus(ApplicationStatus.WITHDRAWN)
        );
    }

    public ApplicationDTO toDto(JobApplication app) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(app.getId());
        dto.setCompanyName(app.getCompanyName());
        dto.setJobTitle(app.getJobTitle());
        dto.setJobUrl(app.getJobUrl());
        dto.setLocation(app.getLocation());
        dto.setStatus(app.getStatus());
        dto.setAppliedDate(app.getAppliedDate());
        dto.setNotes(app.getNotes());
        dto.setContactPerson(app.getContactPerson());
        dto.setContactEmail(app.getContactEmail());
        return dto;
    }

    private void mapDtoToEntity(ApplicationDTO dto, JobApplication app) {
        app.setCompanyName(dto.getCompanyName());
        app.setJobTitle(dto.getJobTitle());
        app.setJobUrl(dto.getJobUrl());
        app.setLocation(dto.getLocation());
        app.setStatus(dto.getStatus());
        app.setAppliedDate(dto.getAppliedDate());
        app.setNotes(dto.getNotes());
        app.setContactPerson(dto.getContactPerson());
        app.setContactEmail(dto.getContactEmail());
    }
}
