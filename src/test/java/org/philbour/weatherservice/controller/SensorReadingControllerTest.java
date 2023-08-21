package org.philbour.weatherservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.philbour.weatherservice.model.MetricValue;
import org.philbour.weatherservice.model.SensorReading;
import org.philbour.weatherservice.model.resource.SensorReadingResource;
import org.philbour.weatherservice.service.SensorReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = SensorReadingController.class)
@AutoConfigureMockMvc(addFilters = false)
class SensorReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorReadingService sensorReadingService;

    private SensorReadingResource readingResource;

    private static final Long ID = 1L;
    private static final Long SENSOR_ID = 2L;
    private static final String TEMPERATURE_TYPE = "temperature";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupClass() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setup() {
        readingResource = createReadingResource();
    }

    @Test
    void register_ValidReading_ResourceSaved() throws Exception {
        when(sensorReadingService.register(isA(SensorReadingResource.class))).thenReturn(new SensorReading(1L, 2L,
                LocalDateTime.now(), Collections.emptyList()));

        MvcResult mvcResult = mockMvc.perform(post("/reading").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingResource))).andExpect(status().isCreated()).andReturn();

        SensorReading reading = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                SensorReading.class);
        assertNotNull(reading);
        assertThat(reading.getId()).isNotNull();
    }

    @Test
    void register_DateInFuture_ReturnsBadRequest() throws Exception {
        SensorReadingResource readingResource = createReadingResource(LocalDateTime.now().plusHours(1));

        mockMvc.perform(post("/reading").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readingResource))).andExpect(status().isBadRequest());
    }

    @Test
    void getAll_ReadingsFound_ReturnsReadings() throws Exception {
        when(sensorReadingService.getAll()).thenReturn(Arrays.asList(new SensorReading(readingResource)));

        MvcResult mvcResult = mockMvc.perform(get("/reading")).andExpect(status().isOk()).andReturn();

        List<SensorReading> readings = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, SensorReading.class));
        assertThat(readings).hasSize(1);
    }

    @Test
    void getAll_NoReadingsFound_ReturnsEmptyBody() throws Exception {
        when(sensorReadingService.getAll()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get("/reading"))
                .andExpect(status().isOk())
                .andReturn();

        List<SensorReading> readings = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, SensorReading.class));
        assertThat(readings).isEmpty();
    }

    @Test
    void getById_ReadingFound_ReturnsReading() throws Exception {
        when(sensorReadingService.getById(ID)).thenReturn(Optional.of(new SensorReading(ID, readingResource.getSensorId(),
                readingResource.getTimeOfReading(), readingResource.getMetrics())));

        MvcResult mvcResult = mockMvc.perform(get("/reading/{id}", ID)).andExpect(status().isOk()).andReturn();

        SensorReading reading = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SensorReading.class);
        assertNotNull(reading);
        assertEquals(ID, reading.getId());
    }

    @Test
    void getById_ReadingNotFound_Returns404() throws Exception {
        when(sensorReadingService.getById(ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/reading/{id}", ID)).andExpect(status().isNotFound()).andExpect(content().string(StringUtils.EMPTY));
    }

    @Test
    void deleteById_ReadingDeleted_Returns204() throws Exception {
        mockMvc.perform(delete("/reading/{id}", ID))
                .andExpect(status().isNoContent())
                .andExpect(content().string(StringUtils.EMPTY));
    }

    @Test
    void getSensorValueByMetric_ValidValues_ReturnsAverageValue() throws Exception {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();
        when(sensorReadingService.getMetricAverageBySensorId(SENSOR_ID, TEMPERATURE_TYPE, from, to)).thenReturn(1);

        MvcResult mvcResult = mockMvc.perform(get("/reading/sensor/{id}/metric/{type}", SENSOR_ID, TEMPERATURE_TYPE)
                .queryParam("queryType", "avg")
                .queryParam("from", from.toString())
                .queryParam("to", to.toString())).andExpect(status().isOk()).andReturn();

        Integer val = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Integer.class);
        assertEquals(1, val);
    }

    @Test
    void getValueByMetric_ValidValues_ReturnsAverageValue() throws Exception {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now();
        when(sensorReadingService.getMetricAverage(TEMPERATURE_TYPE, from, to)).thenReturn(10);

        MvcResult mvcResult = mockMvc.perform(get("/reading/sensor/metric/{type}", TEMPERATURE_TYPE).queryParam(
                "queryType", "avg").queryParam("from", from.toString()).queryParam("to", to.toString()))
                .andExpect(status().isOk())
                .andReturn();

        Integer val = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Integer.class);
        assertEquals(10, val);
    }

    private SensorReadingResource createReadingResource() {
        return createReadingResource(LocalDateTime.now());
    }

    private SensorReadingResource createReadingResource(LocalDateTime readingDate) {
        List<MetricValue> metrics = new ArrayList<>();
        metrics.add(new MetricValue(1L, 22));
        metrics.add(new MetricValue(2L, 15));
        metrics.add(new MetricValue(3L, 64));
        metrics.add(new MetricValue(4L, 1016));

        return new SensorReadingResource(SENSOR_ID, readingDate, metrics);
    }

}
