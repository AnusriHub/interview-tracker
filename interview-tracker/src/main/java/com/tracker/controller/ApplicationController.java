
package com.tracker.controller;

import com.tracker.dto.ApplicationDTO;
import com.tracker.model.ApplicationStatus;
import com.tracker.model.JobApplication;
import com.tracker.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;

    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute("stats", service.getDashboardStats());

        List<JobApplication> recentApps = service.getAllApplications()
                .stream()
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("recentApps", recentApps);
        model.addAttribute("activePage", "dashboard");

        return "dashboard";
    }

    @GetMapping("/applications")
    public String listApplications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Model model) {

        List<JobApplication> applications =
                service.filterApplications(status, search);

        model.addAttribute("applications", applications);
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentSearch", search);
        model.addAttribute("stats", service.getDashboardStats());
        model.addAttribute("activePage", "applications");

        return "applications";
    }

    @GetMapping("/applications/new")
    public String newApplicationForm(Model model) {

        model.addAttribute("application", new ApplicationDTO());
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activePage", "applications");

        return "application-form";
    }

    @PostMapping("/applications/new")
    public String createApplication(
            @Valid @ModelAttribute("application") ApplicationDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {

            model.addAttribute("statuses", ApplicationStatus.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "applications");

            return "application-form";
        }

        service.save(dto);

        redirectAttrs.addFlashAttribute(
                "successMessage",
                "Application added for " + dto.getCompanyName() + "!"
        );

        return "redirect:/applications";
    }

    @GetMapping("/applications/{id}/edit")
    public String editApplicationForm(
            @PathVariable Long id,
            Model model) {

        JobApplication application = service.getById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));

        model.addAttribute(
                "application",
                service.toDto(application)
        );

        model.addAttribute(
                "statuses",
                ApplicationStatus.values()
        );

        model.addAttribute("isEdit", true);
        model.addAttribute("activePage", "applications");

        return "application-form";
    }

    @PostMapping("/applications/{id}/edit")
    public String updateApplication(
            @PathVariable Long id,
            @Valid @ModelAttribute("application") ApplicationDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "statuses",
                    ApplicationStatus.values()
            );

            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "applications");

            return "application-form";
        }

        service.update(id, dto);

        redirectAttrs.addFlashAttribute(
                "successMessage",
                "Application updated for " + dto.getCompanyName() + "!"
        );

        return "redirect:/applications";
    }

    @GetMapping("/applications/{id}")
    public String viewApplication(
            @PathVariable Long id,
            Model model) {

        JobApplication application = service.getById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));

        model.addAttribute("app", application);
        model.addAttribute("activePage", "applications");

        return "application-detail";
    }

    @PostMapping("/applications/{id}/delete")
    public String deleteApplication(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {

        service.getById(id).ifPresent(application -> {

            service.delete(id);

            redirectAttrs.addFlashAttribute(
                    "successMessage",
                    "Application for "
                            + application.getCompanyName()
                            + " deleted."
            );
        });

        return "redirect:/applications";
    }

    @PostMapping("/applications/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            RedirectAttributes redirectAttrs) {

        service.getById(id).ifPresent(application -> {

            ApplicationDTO dto = service.toDto(application);
            dto.setStatus(status);

            service.update(id, dto);

            redirectAttrs.addFlashAttribute(
                    "successMessage",
                    "Status updated to "
                            + status.getDisplayName()
            );
        });

        return "redirect:/applications/" + id;
    }
}

