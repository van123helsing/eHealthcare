package com.example.appointments.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@FieldNameConstants
@Entity
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name= "patient", nullable = false, length = 50)
    private String patient;

    @Column(name= "schedule_id", nullable = false)
    private Long schedule_id;

    @Column(name= "service", nullable = false)
    private String service;
}
