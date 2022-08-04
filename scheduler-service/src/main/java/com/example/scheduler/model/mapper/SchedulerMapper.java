package com.example.scheduler.model.mapper;

import com.example.scheduler.domain.Schedules;
import com.example.scheduler.model.SchedulerDto;
import com.example.scheduler.model.SchedulerInput;
import org.mapstruct.Mapper;

@Mapper
public interface SchedulerMapper {

    SchedulerDto toDto(Schedules schedule);
    Schedules toEntity(SchedulerInput schedulerInput);

}
