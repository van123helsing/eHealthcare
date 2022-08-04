package com.example.scheduler.repository;

import com.example.scheduler.domain.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<Schedules, Long> {


}
