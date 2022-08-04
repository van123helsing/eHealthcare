package com.example.appointments.service;

import com.example.appointments.domain.Appointments;
import com.example.appointments.model.AppointmentsDto;
import com.example.appointments.model.AppointmentsInput;
import org.springframework.util.MultiValueMap;

public interface AppointmentsService {

    AppointmentsDto save(AppointmentsInput appointmentsInput, String patient);

    Appointments getById(Long id);

    void delete(Long id);

    void setFlagInScheduler(Long schedule, boolean flag, MultiValueMap<String, String> headers);
}
