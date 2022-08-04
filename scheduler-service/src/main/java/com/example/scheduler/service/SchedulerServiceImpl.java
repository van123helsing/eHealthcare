package com.example.scheduler.service;

import com.example.scheduler.domain.Schedules;
import com.example.scheduler.domain.Services;
import com.example.scheduler.errorhadler.ResourceNotFoundException;
import com.example.scheduler.model.SchedulerDto;
import com.example.scheduler.model.SchedulerInput;
import com.example.scheduler.model.mapper.SchedulerMapper;
import com.example.scheduler.repository.SchedulerRepository;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerRepository repository;
    private final SchedulerMapper mapper;

    @Transactional
    public List<SchedulerDto> save(List<SchedulerInput> schedulerInputs, String doctor) {

        List<SchedulerDto> schedulerDtos = new ArrayList<>();

        for (SchedulerInput schedulerInput: schedulerInputs) {
            Schedules schedule = mapper.toEntity(schedulerInput);
            schedule.setDoctor(doctor);

            schedulerDtos.add(mapper.toDto(repository.save(schedule)));
        }


        return schedulerDtos;
    }

    @Transactional
    public SchedulerDto update(SchedulerInput schedulerInput, Schedules schedule) {

        if(schedulerInput.getInterval_start() != null)
            schedule.setInterval_start(schedulerInput.getInterval_start());

        if(schedulerInput.getInterval_end() != null)
            schedule.setInterval_end(schedulerInput.getInterval_end());

        return mapper.toDto(repository.save(schedule));
    }

    @Transactional
    public SchedulerDto createAppointment(Schedules schedule, boolean flag) {

        schedule.setStill_available(!flag);

        return mapper.toDto(repository.save(schedule));
    }

    public Schedules getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedules", "id", id));
    }

    public List<SchedulerDto> get(String doctor, LocalDateTime fromDate, Services service, boolean still_available) {
        Stream<SchedulerDto> schedulerDtoStream = repository.findAll(Sort.by(Schedules.Fields.doctor)).stream().map(mapper::toDto);

        // filter out the doctor
        if (!Strings.isNullOrEmpty(doctor))
            schedulerDtoStream = schedulerDtoStream.filter(d -> d.getDoctor().equals(doctor));

        // filter out schedules which are after fromDate
        if (fromDate == null)
            fromDate = LocalDateTime.now();

        Timestamp finalFromDate = Timestamp.valueOf(fromDate);
        schedulerDtoStream = schedulerDtoStream.filter(d -> d.getInterval_start().after(finalFromDate));

        // filter out only schedules which contains service
        if (service != null && service != Services.ANY)
            schedulerDtoStream = schedulerDtoStream.filter(d -> Arrays.stream(d.getServices())
                    .anyMatch(x -> service.toString().equals(x.toUpperCase())));

        // filter out only still available schedules
        if (still_available)
            schedulerDtoStream = schedulerDtoStream.filter(SchedulerDto::isStill_available);

        return schedulerDtoStream.collect(Collectors.toList());
    }



    @Override
    public void delete(Long id) {
        if (!this.repository.existsById(id)) {
            throw new IllegalArgumentException(id + " id does not exist in schedules table.");
        }
        repository.deleteById(id);
    }
}
