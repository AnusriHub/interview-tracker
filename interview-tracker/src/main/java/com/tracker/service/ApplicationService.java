
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
        ApplicationStatus applicationStatus = null;

        if (status != null && !status.isEmpty()) {
            try {
                applicationStatus = ApplicationStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                applicationStatus = null;
            }
        }

        String searchTerm = null;

        if (search != null && !search.trim().isEmpty()) {
            searchTerm = search.trim();
        }

        return repository.findByFilters(applicationStatus, searchTerm);
    }

    public Optional<JobApplication> getById(Long id) {
        return repository.findById(id);
    }

    public JobApplication save(ApplicationDTO dto) {
        JobApplication application = new JobApplication();

        mapDtoToEntity(dto, application);

        return repository.save(application);
    }

    public JobApplication update(Long id, ApplicationDTO dto) {
        JobApplication application = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));

        mapDtoToEntity(dto, application);

        return repository.save(application);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public DashboardStats getDashboardStats() {
        long total = repository.count();

        long applied = repository.countByStatus(ApplicationStatus.APPLIED);
        long oa = repository.countByStatus(ApplicationStatus.OA);
        long interview = repository.countByStatus(ApplicationStatus.INTERVIEW);
        long offer = repository.countByStatus(ApplicationStatus.OFFER);
        long rejected = repository.countByStatus(ApplicationStatus.REJECTED);
        long withdrawn = repository.countByStatus(ApplicationStatus.WITHDRAWN);

        return new DashboardStats(
                total,
                applied,
                oa,
                interview,
                offer,
                rejected,
                withdrawn
        );
    }

    public ApplicationDTO toDto(JobApplication application) {
        ApplicationDTO dto = new ApplicationDTO();

        dto.setId(application.getId());
        dto.setCompanyName(application.getCompanyName());
        dto.setJobTitle(application.getJobTitle());
        dto.setJobUrl(application.getJobUrl());
        dto.setLocation(application.getLocation());
        dto.setStatus(application.getStatus());
        dto.setAppliedDate(application.getAppliedDate());
        dto.setNotes(application.getNotes());
        dto.setContactPerson(application.getContactPerson());
        dto.setContactEmail(application.getContactEmail());

        return dto;
    }

    private void mapDtoToEntity(ApplicationDTO dto, JobApplication application) {
        application.setCompanyName(dto.getCompanyName());
        application.setJobTitle(dto.getJobTitle());
        application.setJobUrl(dto.getJobUrl());
        application.setLocation(dto.getLocation());
        application.setStatus(dto.getStatus());
        application.setAppliedDate(dto.getAppliedDate());
        application.setNotes(dto.getNotes());
        application.setContactPerson(dto.getContactPerson());
        application.setContactEmail(dto.getContactEmail());
    }
}

