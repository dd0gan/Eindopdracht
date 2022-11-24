package com.example.les17.controller;

import com.example.les17.dto.MessageDto;
import com.example.les17.dto.VacancyDto;
import com.example.les17.service.VacancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("")
    public ResponseEntity getAllVacancies() {
        List<VacancyDto> vacancies = vacancyService.getAllVacancies();
        return ResponseEntity.ok().body(vacancies);
    }

    @GetMapping("/{id}")
    public ResponseEntity getVacancy(@PathVariable Integer id) {
        VacancyDto vacancy = vacancyService.getVacancy(id);
        return ResponseEntity.ok().body(vacancy);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity createVacancy(@RequestBody VacancyDto vacancyDto) {
        VacancyDto savedVacancy = vacancyService.createVacancy(vacancyDto);
        return ResponseEntity.ok().body(savedVacancy);
    }

    @PutMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity updateVacancy(@RequestBody VacancyDto vacancyDto) {
        VacancyDto savedVacancy = vacancyService.updateVacancy(vacancyDto);
        return ResponseEntity.ok().body(savedVacancy);
    }

    @PostMapping("/apply")
    public ResponseEntity apply(@RequestParam Integer id) {
        return ResponseEntity.ok().body(vacancyService.apply(id));
    }

    @PostMapping("/complete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity complete(@RequestParam Integer id, @RequestParam Integer applicationId, @RequestParam String acceptReject) {
        return ResponseEntity.ok().body(vacancyService.complete(id,applicationId,acceptReject));
    }
}
