package com.example.appointments.model.mapper;

import com.example.appointments.domain.Appointments;
import com.example.appointments.model.AppointmentsDto;
import com.example.appointments.model.AppointmentsInput;
import org.mapstruct.Mapper;

@Mapper
public interface AppointmentsMapper {

    AppointmentsDto toDto(Appointments appointments);
    Appointments toEntity(AppointmentsInput appointmentsInput);

}
