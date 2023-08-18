package org.philbour.weatherservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.philbour.weatherservice.model.MetricValue;
import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupClass() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void register_ValidReading_ResourceSaved() throws Exception {
        SensorReadingResource readingResource = createReading();

        MvcResult mvcResult = mockMvc.perform(post("/register").contentType("application/json")
                .content(objectMapper.writeValueAsString(readingResource))).andExpect(status().isCreated()).andReturn();

        SensorReading reading = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                SensorReading.class);
        assertNotNull(reading);
        assertThat(reading.getId()).isNotBlank();
    }

    @Test
    void register_DateInFuture_ReturnsBadRequest() throws Exception {
        SensorReadingResource readingResource = createReading(LocalDateTime.now().plusHours(1));

        mockMvc.perform(post("/register").contentType("application/json")
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