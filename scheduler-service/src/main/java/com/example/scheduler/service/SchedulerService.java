package com.example.scheduler.service;

import com.example.scheduler.domain.Schedules;
import com.example.scheduler.domain.Services;
import com.example.scheduler.model.SchedulerDto;
import com.example.scheduler.model.SchedulerInput;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerService {

    List<SchedulerDto> save(List<SchedulerInput> schedulerInput, String doctor);

    SchedulerDto update(SchedulerInput schedulerInput, Schedules schedulerDto);

    SchedulerDto createAppointment(Schedules schedulerDto, boolean flag);

    Schedules getById(Long id);

    List<SchedulerDto> get(String doctor, LocalDateTime fromDate, Services service, boolean still_available);

    void delete(Long id);
}
