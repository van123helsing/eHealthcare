package com.example.surveys.model.mapper;

import com.example.surveys.domain.Surveys;
import com.example.surveys.model.SurveysDto;
import com.example.surveys.model.SurveysInput;
import org.mapstruct.Mapper;

@Mapper
public interface SurveysMapper {

    SurveysDto toDto(Surveys survey);
    Surveys toEntity(SurveysInput surveysInput);

}
