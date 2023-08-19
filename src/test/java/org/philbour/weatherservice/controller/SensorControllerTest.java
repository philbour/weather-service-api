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
import org.junit.jupiter.api.Test;
import org.philbour.weatherservice.model.Sensor;
import org.philbour.weatherservice.model.resource.SensorResource;
import org.philbour.weatherservice.service.SensorService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@WebMvcTest(controllers = SensorController.class)
@AutoConfigureMockMvc(addFilters = false)
class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorService sensorService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String LOCATION_NAME = "location";

    @BeforeAll
    static void setupClass() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Test
    void register_ValidSensor_ResourceSaved() throws Exception {
        SensorResource sensorResource = new SensorResource(LOCATION_NAME);
        when(sensorService.register(isA(Sensor.class))).thenReturn(new Sensor(1L, LOCATION_NAME));

        MvcResult mvcResult = mockMvc.perform(post("/sensor").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sensorResource))).andExpect(status().isCreated()).andReturn();

        Sensor sensor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Sensor.class);
        assertNotNull(sensor);
        assertThat(sensor.getId()).isNotNull();
    }

    @Test
    void register_SensorNotValid_ReturnsBadRequest() throws Exception {
        SensorResource sensorResource = new SensorResource("");

        mockMvc.perform(post("/sensor").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sensorResource))).andExpect(status().isBadRequest());
    }

    @Test
    void getAll_SensorsFound_ReturnsSensors() throws Exception {
        when(sensorService.getAll()).thenReturn(createSensors(3));

        MvcResult mvcResult = mockMvc.perform(get("/sensor")).andExpect(status().isOk()).andReturn();

        List<Sensor> sensors = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, Sensor.class));
        assertThat(sensors).hasSize(3);
    }

    @Test
    void getAll_NoSensorsFound_ReturnsEmptyBody() throws Exception {
        when(sensorService.getAll()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get("/sensor"))
                .andExpect(status().isOk())
                .andReturn();

        List<Sensor> sensors = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, Sensor.class));
        assertThat(sensors).isEmpty();
    }

    @Test
    void getById_SensorFound_ReturnsSensor() throws Exception {
        when(sensorService.getById(1L)).thenReturn(Optional.of(new Sensor(1L, LOCATION_NAME)));

        MvcResult mvcResult = mockMvc.perform(get("/sensor/{id}", 1L)).andExpect(status().isOk()).andReturn();

        Sensor sensor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Sensor.class);
        assertNotNull(sensor);
        assertEquals(1L, sensor.getId());
    }

    @Test
    void getById_SensorNotFound_Returns404() throws Exception {
        when(sensorService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/sensor/{id}", 1L)).andExpect(status().isNotFound()).andExpect(content().string(StringUtils.EMPTY));
    }

    @Test
    void deleteById_SensorDeleted_Returns204() throws Exception {
        mockMvc.perform(delete("/sensor/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(StringUtils.EMPTY));
    }

    private List<Sensor> createSensors(int numToCreate) {
        List<Sensor> sensors = new ArrayList<>();
        IntStream.range(0, numToCreate).forEach(num -> sensors.add(this.createSensor(num)));
        return sensors;
    }

    private Sensor createSensor(int i) {
        return new Sensor((long)i, LOCATION_NAME.concat(" ") + i);
    }

}
