package com.example.scheduler.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SchedulerInput {

    private Timestamp interval_start;

    private Timestamp interval_end;

    private String[] services;
}
