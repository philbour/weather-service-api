package org.philbour.weatherservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.philbour.weatherservice.model.MetricValue;
import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.philbour.weatherservice.model.resource.SensorResource;
import org.philbour.weatherservice.repository.SensorRepository;
import org.philbour.weatherservice.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = SensorController.class)
@AutoConfigureMockMvc(addFilters = false)
class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupClass() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Test
    void register_ValidSensor_ResourceSaved() throws Exception {
        SensorResource sensorResource = new SensorResource("my location");
        when(sensorService.register(isA(Sensor.class))).thenReturn(new Sensor(1L, "my location"));

        MvcResult mvcResult = mockMvc.perform(post("/sensor/register").contentType("application/json")
                .content(objectMapper.writeValueAsString(sensorResource))).andExpect(status().isCreated()).andReturn();

        Sensor sensor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Sensor.class);
        assertNotNull(sensor);
        assertThat(sensor.getId()).isNotNull();
        System.out.println(sensor.getId());
    }

    // @Test
    void register_DateInFuture_ReturnsBadRequest() throws Exception {
        SensorReadingResource readingResource = createReading(LocalDateTime.now().plusHours(1));

        mockMvc.perform(post("/reading/register").contentType("application/json")
                .content(objectMapper.writeValueAsString(readingResource))).andExpect(status().isBadRequest());
    }

    private SensorReadingResource createReading() {
        return createReading(LocalDateTime.now());
    }

    private SensorReadingResource createReading(LocalDateTime readingDate) {
        List<MetricValue> metrics = new ArrayList<>();
        metrics.add(new MetricValue("tmp", "22"));
        metrics.add(new MetricValue("wnd", "15"));
        metrics.add(new MetricValue("hum", "64"));
        metrics.add(new MetricValue("air", "1016"));

        return new SensorReadingResource("1", readingDate, metrics);
    }

}
