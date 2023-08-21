weather-service-api
========

![](logo.png)

# Intro
REST API service for registering and querying weather sensor data.

# Features

* CRUD (Create, Read, Delete) operations for metrics, sensors, and sensor readings
* Data written to Database
* Can be execute as a stand-alone Spring boot app or run within a Docker container
* Data persisted across restarts of service or container
* Custom queries to get average values of certain metrics
* New Metrics and Sensors can be easily added

# Limitations 

* The ability to update a resource currently not supported

# Execute

Can be run in 2 ways - 
## Stand-alone Spring boot

### Steps

1. Clone repo
2. Run with IDE or on cli
    1. Import into IDE
        1. Within the IDE, run as Spring Boot App
    2. Run from command line
        1. Build project `mvn clean install -Ddockerfile.skip`
        2. Execute `java -jar weather-service.jar`

## Docker

### Steps

1. Clone repo
2. Run maven install - `mvn clean install`. This will build the jar and also the docker image
3. From the command line run - `docker run -v /data:/data -p 8080:8080 weather-service-api:0.0.1-SNAPSHOT`

# Usage

To view all sensors for example browse to - 
[localhost:8080/weatherservice/api/sensor](http://localhost:8080/weatherservice/api/sensor)

## Authentication

All endpoints are secured using basic auth. Delete endpoints required the ADMIN role

| Role       | Username  | Password      |
| ---------- | --------- | ------------- |
| ADMIN      | admin     | imtheboss     |   
| USER       | user      | letmein       |


# Docs

OpenAPI docs available at [localhost:8080/weatherservice/api/docs](http://localhost:8080/weatherservice/api/docs)  
Swagger docs available at [http://localhost:8080/weatherservice/api/docs/swagger-ui/](http://localhost:8080/weatherservice/api/docs/swagger-ui/) 

# Examples


# Future

* Allow a resource (Metric, Sensor) to be updated
* Additional queries (min, max, etc...)
* Versioned API
* Caching
* Integration tests
* Better logging
* Better API doces
* Proper Auth using a token
