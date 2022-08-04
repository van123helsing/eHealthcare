package com.example.surveys.service;

import com.example.surveys.model.SurveysDto;
import com.example.surveys.model.SurveysInput;
import com.example.surveys.model.SurveysOutput;
import org.springframework.util.MultiValueMap;

public interface SurveysService {

    void save(SurveysInput surveysInput, MultiValueMap<String, String> headers);

    SurveysOutput getByAppointmentId(Long id, MultiValueMap<String, String> headers);

}
