package com.example.appointments.service;

import com.example.appointments.common.model.Schedule;
import com.example.appointments.domain.Appointments;
import com.example.appointments.errorhadler.ResourceNotFoundException;
import com.example.appointments.model.AppointmentsDto;
import com.example.appointments.model.AppointmentsInput;
import com.example.appointments.model.mapper.AppointmentsMapper;
import com.example.appointments.repository.AppointmentsRepository;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AppointmentsServiceImpl implements AppointmentsService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EurekaClient eurekaClient;
    private static final String serviceId = "SCHEDULER-SERVICE";
    private final AppointmentsRepository repository;
    private final AppointmentsMapper mapper;

    @Transactional
    public AppointmentsDto save(AppointmentsInput appointmentsInput, String patient) {

        Appointments appointments = mapper.toEntity(appointmentsInput);
        appointments.setPatient(patient);

        return mapper.toDto(repository.save(appointments));
    }

    public Appointments getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
    }

    @Override
    public void delete(Long id) {
        if (!this.repository.existsById(id)) {
            throw new IllegalArgumentException(id + " id does not exist in appointments table.");
        }
        repository.deleteById(id);
    }


    public void setFlagInScheduler(Long schedule, boolean flag, MultiValueMap<String, String> headers){

        var patient = headers.getFirst("username");
        var token = headers.getFirst("authorization");
        var authoritiesStr = headers.getFirst("authorities");

        Application application = eurekaClient.getApplication(serviceId);
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String url = "http://" + instanceInfo.getIPAddr() + ":"
                + instanceInfo.getPort() + "/api/scheduler/appointment/" + schedule +"?apply=" + flag;

        HttpEntity<String> entity = setHeaders(patient, token, authoritiesStr);

        restTemplate.exchange(url, HttpMethod.PUT, entity, Schedule.class);

        //TODO check if successful
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
