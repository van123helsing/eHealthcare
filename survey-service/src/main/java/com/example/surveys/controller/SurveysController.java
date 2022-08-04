package com.example.surveys.controller;

import com.example.surveys.model.SurveysDto;
import com.example.surveys.model.SurveysInput;
import com.example.surveys.model.SurveysOutput;
import com.example.surveys.service.SurveysService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/surveys")
@RequiredArgsConstructor
public class SurveysController {

    private final SurveysService service;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<String> saveRatingForAppointment(@RequestBody @Valid SurveysInput surveysInput,
                                           @RequestHeader MultiValueMap<String, String> headers) {

        this.service.save(surveysInput, headers);
        return ResponseEntity.status(HttpStatus.CREATED).body("Rating is being processed!");
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<SurveysOutput> getSurveyByAppointmentId(@PathVariable(value = "id") Long appointment,
                                                                  @RequestHeader MultiValueMap<String, String> headers) {
        return ResponseEntity.ok(this.service.getByAppointmentId(appointment, headers));
    }
}
