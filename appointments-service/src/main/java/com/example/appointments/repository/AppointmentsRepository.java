package com.example.appointments.repository;

import com.example.appointments.domain.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {


}
