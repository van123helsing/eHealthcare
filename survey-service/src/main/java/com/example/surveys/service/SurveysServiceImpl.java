package com.example.surveys.service;

import com.example.surveys.common.model.Appointment;
import com.example.surveys.common.model.Schedule;
import com.example.surveys.domain.Surveys;
import com.example.surveys.model.SurveysInput;
import com.example.surveys.model.SurveysOutput;
import com.example.surveys.repository.SurveysRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SurveysServiceImpl implements SurveysService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EurekaClient eurekaClient;
    private static final String schedulerServiceId = "SCHEDULER-SERVICE";
    private static final String appointmentsServiceId = "APPOINTMENTS-SERVICE";
    private final SurveysRepository repository;

    @Transactional
    @Async
    public void save(SurveysInput surveysInput, MultiValueMap<String, String> headers) {

        var patient = headers.getFirst("username");
        var token = headers.getFirst("authorization");
        var authoritiesStr = headers.getFirst("authorities");

        Appointment appointment = getAppointment(surveysInput.getAppointment(), patient, token, authoritiesStr);

        Schedule schedule = getSchedule(appointment.getSchedule_id(), patient, token, authoritiesStr);

        Integer rating = surveysInput.getRating();

        if(rating <= 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be in range from 1 to 5!");
        }

        Surveys survey = new Surveys();
        survey.setAppointment_id(surveysInput.getAppointment());
        survey.setRating(rating);

        repository.save(survey);
    }

    public SurveysOutput getByAppointmentId(Long appointmentId, MultiValueMap<String, String> headers) {

        var patient = headers.getFirst("username");
        var token = headers.getFirst("authorization");
        var authoritiesStr = headers.getFirst("authorities");

        Appointment appointment = getAppointment(appointmentId, patient, token, authoritiesStr);

        Schedule schedule = getSchedule(appointment.getSchedule_id(), patient, token, authoritiesStr);

        SurveysOutput surveysOutput = new SurveysOutput();
        surveysOutput.setPatient(patient);
        surveysOutput.setDoctor(schedule.getDoctor());
        surveysOutput.setService(appointment.getService());
        surveysOutput.setInterval_start(schedule.getInterval_start());
        surveysOutput.setInterval_end(schedule.getInterval_end());

        return surveysOutput;
    }

    private Appointment getAppointment(Long appointmentId, String patient, String token, String authoritiesStr){
        Application application = eurekaClient.getApplication(appointmentsServiceId);
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String url = "http://" + instanceInfo.getIPAddr() + ":"
                + instanceInfo.getPort() + "/api/appointments/" + appointmentId;

        HttpEntity<String> entity = setHeaders(patient, token, authoritiesStr);

        Appointment appointment = restTemplate.exchange(url, HttpMethod.GET, entity, Appointment.class).getBody();

        if (appointment == null){
            throw new IllegalArgumentException("Appointment with id " + appointmentId + " does not exist!");
        }

        if (!appointment.getPatient().equals(patient)) {
            throw new IllegalArgumentException(patient
                    + " user is not allowed to view survey of appointment with id " + appointmentId);
        }

        return appointment;
    }

    private Schedule getSchedule(Long scheduleId, String patient, String token, String authoritiesStr){
        Application application = eurekaClient.getApplication(schedulerServiceId);
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String url = "http://" + instanceInfo.getIPAddr() + ":"
                + instanceInfo.getPort() + "/api/scheduler/" + scheduleId;

        HttpEntity<String> entity = setHeaders(patient, token, authoritiesStr);

        Schedule schedule = restTemplate.exchange(url, HttpMethod.GET, entity, Schedule.class).getBody();

        if (schedule == null){
            throw new IllegalArgumentException("Schedule with id " + scheduleId + " does not exist!");
        }

        if (schedule.getInterval_end().after(new Timestamp(System.currentTimeMillis()))){
            throw new IllegalArgumentException("This survey can not yet be completed. Appointment ends at: "
                    + schedule.getInterval_end());
        }

        return schedule;
    }

    private static HttpEntity<String> setHeaders(String patient, String token, String authoritiesStr) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", token);
        headers.add("username", patient);
        headers.add("authorities", authoritiesStr);

        return new HttpEntity<>("body", headers);
    }
}
