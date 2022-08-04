package com.example.surveys.repository;

import com.example.surveys.domain.Surveys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveysRepository extends JpaRepository<Surveys, Long> {


}
