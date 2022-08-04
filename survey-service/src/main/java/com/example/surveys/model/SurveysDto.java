package com.example.surveys.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SurveysDto {

    private Long id;

    private String patient;

    private Long schedule_id;

    private String service;
}
