package com.tracker.dto;

import com.tracker.model.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ApplicationDTO {

    private Long id;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Job title is required")
    private String jobTitle;

    private String jobUrl;
    private String location;

    @NotNull(message = "Status is required")
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate appliedDate = LocalDate.now();

    private String notes;
    private String contactPerson;
    private String contactEmail;
}
