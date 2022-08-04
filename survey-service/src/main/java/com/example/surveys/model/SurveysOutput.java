package com.example.surveys.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SurveysOutput {

    private String patient;

    private String doctor;

    private String service;

    private Timestamp interval_start;

    private Timestamp interval_end;

}
