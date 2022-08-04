package com.example.appointments.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SchedulerDto {

    private Long id;

    private String doctor;

    private Timestamp interval_start;

    private Timestamp interval_end;

    private String[] services;

    private boolean still_available;
}
