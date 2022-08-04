package com.example.scheduler.controller;

import com.example.scheduler.domain.Schedules;
import com.example.scheduler.domain.Services;
import com.example.scheduler.model.SchedulerDto;
import com.example.scheduler.model.SchedulerInput;
import com.example.scheduler.model.mapper.SchedulerMapper;
import com.example.scheduler.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {

    private final SchedulerService service;

    private final SchedulerMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<SchedulerDto>> save(@RequestBody @Valid List<SchedulerInput> schedulerInput,
                                            @RequestHeader MultiValueMap<String, String> headers) {
        var doctor = headers.getFirst("username");
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(schedulerInput, doctor));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_DOCTOR')")
    public ResponseEntity<List<SchedulerDto>> get(@RequestParam(name = "doctor", required=false) String doctor,
                                            @RequestParam(name = "fromDate", required=false) LocalDateTime fromDate,
                                            @RequestParam(name = "service", required=false) Services service,
                                            @RequestParam(name = "availability", required=false) boolean still_available) {
        return ResponseEntity.ok(this.service.get(doctor, fromDate, service, still_available));
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_DOCTOR')")
    public ResponseEntity<SchedulerDto> getById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(mapper.toDto(this.service.getById(id)));
    }

    @PutMapping(value = "{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<SchedulerDto> update(@RequestBody @Valid SchedulerInput schedulerInput,
                                               @PathVariable(value = "id") Long id) {
        Schedules schedule = this.service.getById(id);
        return ResponseEntity.ok(this.service.update(schedulerInput, schedule));
    }

    @PutMapping(value = "/appointment/{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<SchedulerDto> appointment(@PathVariable(value = "id") Long id,
                                                    @RequestParam(name = "apply") boolean flag) {
        Schedules schedule = this.service.getById(id);
        return ResponseEntity.ok(this.service.createAppointment(schedule, flag));
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public void delete(@PathVariable(value = "id") Long id) {
        this.service.delete(id);
    }
}
