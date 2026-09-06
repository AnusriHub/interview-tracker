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

@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("stats", service.getDashboardStats());
        model.addAttribute("recentApps", service.getAllApplications().stream().limit(5).collect(java.util.stream.Collectors.toList()));
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }

    @GetMapping("/applications")
    public String listApplications(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Model model) {
        List<JobApplication> apps = service.filterApplications(status, search);
        model.addAttribute("applications", apps);
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
            return "application-form";
        }
        service.save(dto);
        redirectAttrs.addFlashAttribute("successMessage", "Application added for " + dto.getCompanyName() + "!");
        return "redirect:/applications";
    }

    @GetMapping("/applications/{id}/edit")
    public String editApplicationForm(@PathVariable Long id, Model model) {
        JobApplication app = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        model.addAttribute("application", service.toDto(app));
        model.addAttribute("statuses", ApplicationStatus.values());
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
            model.addAttribute("statuses", ApplicationStatus.values());
            model.addAttribute("isEdit", true);
            return "application-form";
        }
        service.update(id, dto);
        redirectAttrs.addFlashAttribute("successMessage", "Application updated for " + dto.getCompanyName() + "!");
        return "redirect:/applications";
    }

    @GetMapping("/applications/{id}")
    public String viewApplication(@PathVariable Long id, Model model) {
        JobApplication app = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        model.addAttribute("app", app);
        model.addAttribute("activePage", "applications");
        return "application-detail";
    }

    @PostMapping("/applications/{id}/delete")
    public String deleteApplication(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        service.getById(id).ifPresent(app -> {
            service.delete(id);
            redirectAttrs.addFlashAttribute("successMessage", "Application for " + app.getCompanyName() + " deleted.");
        });
        return "redirect:/applications";
    }

    @PostMapping("/applications/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            RedirectAttributes redirectAttrs) {
        service.getById(id).ifPresent(app -> {
            ApplicationDTO dto = service.toDto(app);
            dto.setStatus(status);
            service.update(id, dto);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Status updated to " + status.getDisplayName());
        });
        return "redirect:/applications/" + id;
    }
}
