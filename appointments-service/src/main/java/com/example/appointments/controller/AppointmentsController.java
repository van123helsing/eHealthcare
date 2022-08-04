package com.example.appointments.controller;

import com.example.appointments.domain.Appointments;
import com.example.appointments.model.AppointmentsDto;
import com.example.appointments.model.AppointmentsInput;
import com.example.appointments.model.mapper.AppointmentsMapper;
import com.example.appointments.service.AppointmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/appointments")
@RequiredArgsConstructor
public class AppointmentsController {

    private final AppointmentsService service;
    private final AppointmentsMapper mapper;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<AppointmentsDto> save(@RequestBody @Valid AppointmentsInput appointmentsInput,
                                                      @RequestHeader MultiValueMap<String, String> headers) {
        var patient = headers.getFirst("username");
        AppointmentsDto appointmentsDto = this.service.save(appointmentsInput, patient);
        this.service.setFlagInScheduler(appointmentsInput.getSchedule_id(), true, headers);

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentsDto);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void delete(@PathVariable(value = "id") Long id,
                       @RequestHeader MultiValueMap<String, String> headers) {
        Appointments appointments = this.service.getById(id);
        this.service.delete(id);
        this.service.setFlagInScheduler(appointments.getSchedule_id(), false, headers);
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<AppointmentsDto> update(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(mapper.toDto(this.service.getById(id)));
    }
}
