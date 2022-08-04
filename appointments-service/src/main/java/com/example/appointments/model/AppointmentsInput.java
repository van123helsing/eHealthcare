package com.example.appointments.model;

import com.example.appointments.domain.Services;
import lombok.Data;

@Data
public class AppointmentsInput {

    private Services service;

    private Long schedule_id;
}
