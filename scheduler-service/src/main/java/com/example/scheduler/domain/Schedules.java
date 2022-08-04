package com.example.scheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors
@FieldNameConstants
@Entity
public class Schedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(name= "doctor", nullable = false, length = 50)
    private String doctor;

    @Column(name= "interval_start", nullable = false)
    private Timestamp interval_start;

    @Column(name= "interval_end", nullable = false)
    private Timestamp interval_end;

    @Type(type = "com.example.scheduler.GenericArrayUserType")
    @Column(name= "services", nullable = false)
    private String[] services;

    @Column(name= "still_available", nullable = false)
    private boolean still_available;




}
