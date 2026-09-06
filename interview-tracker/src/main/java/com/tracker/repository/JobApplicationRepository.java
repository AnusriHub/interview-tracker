package com.tracker.repository;

import com.tracker.model.ApplicationStatus;
import com.tracker.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStatusOrderByCreatedAtDesc(ApplicationStatus status);

    List<JobApplication> findAllByOrderByCreatedAtDesc();

    List<JobApplication> findByCompanyNameContainingIgnoreCaseOrJobTitleContainingIgnoreCase(
            String companyName, String jobTitle);

    long countByStatus(ApplicationStatus status);

    @Query("SELECT j FROM JobApplication j WHERE " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:search IS NULL OR LOWER(j.companyName) LIKE LOWER(CONCAT('%',:search,'%')) OR " +
           "LOWER(j.jobTitle) LIKE LOWER(CONCAT('%',:search,'%'))) " +
           "ORDER BY j.createdAt DESC")
    List<JobApplication> findByFilters(@Param("status") ApplicationStatus status,
                                        @Param("search") String search);
}
