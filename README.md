# eHealthcare

A simple Spring Boot application that consists of 7 microservices:
1. `config-service`: configuration service, providing settings for every other service
2. `discovery-service`: eureka server, in charge of service discovery
3. `api-gateway`: an entry point to services: **auth-service, scheduler-service, appointment-service** and **surveyy-service**
4. `auth-service`: validates the token passed in request headers
5. `scheduler-service`: managment (CRUD operations) of time slots
6. `appointment-service`: allows creating/deleting appointments for time slots
7. `survey-service`:  retrival of data for a survey and submission of a new survey

![alt text](https://github.com/van123helsing/eHealthcare/blob/main/MicroserviceStructure.png?raw=true)


## Table of Contents

- [Prerequisites](#prerequisites)
- [Postman](#postman)
- [TODOs](#todos)


## Prerequisites

Project is developed in **Java 11** and **Maven** project management tool (make sure to install them both).

**Postgres** is used for database. Install latest version and execute following commands:
```
CREATE DATABASE users;
CREATE DATABASE schedules;
CREATE DATABASE surveys;
CREATE DATABASE appointments;
```

Before building the project few database related properties must be set in files:
- `config-service\src\main\resources\configrepo\appointments-service.yml`
- `config-service\src\main\resources\configrepo\auth-service.yml`
- `config-service\src\main\resources\configrepo\scheduler-service.yml`
- `config-service\src\main\resources\configrepo\surveys-service.yml`

In every file listed above update following properties accordingly to your setup:
```
spring.flyway.url
spring.flyway.user
spring.flyway.password
spring.flyway.locations
spring.datasource.url
spring.datasource.user
spring.datasource.password
```

Build and run services!

## Postman

All endpoints are described (with examples) in postman documentation:
https://documenter.getpostman.com/view/2192547/VUjLJmPn

## TODOs

Room for improvments:
1. Logging/mapping of exceptions
2. Using events for the async processing of surveys (add logic for aggregation of ratings)
3. disable access to `/api/scheduler/appointment/` from api-gateway (only `appointments-service` should be allowed)
4. docker images for DB
