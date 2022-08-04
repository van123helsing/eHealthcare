package com.example.surveys.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Appointment {

    private Long id;

    private String patient;

    private Long schedule_id;

    private String service;
}
