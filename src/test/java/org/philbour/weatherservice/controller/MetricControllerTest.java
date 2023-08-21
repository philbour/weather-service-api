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
import org.philbour.weatherservice.model.Metric;
import org.philbour.weatherservice.model.resource.MetricResource;
import org.philbour.weatherservice.service.MetricService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = MetricController.class)
@AutoConfigureMockMvc(addFilters = false)
class MetricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricService metricService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEMPERATURE_TYPE = "temperature";
    private static final String HUMIDITY_TYPE = "humidity";

    @BeforeAll
    static void setupClass() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Test
    void register_ValidMetric_ResourceSaved() throws Exception {
        MetricResource metricResource = new MetricResource(TEMPERATURE_TYPE);
        when(metricService.register(isA(MetricResource.class))).thenReturn(new Metric(1L, TEMPERATURE_TYPE));

        MvcResult mvcResult = mockMvc.perform(post("/metric").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(metricResource))).andExpect(status().isCreated()).andReturn();

        Metric metric = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Metric.class);
        assertNotNull(metric);
        assertThat(metric.getId()).isNotNull();
    }

    @Test
    void register_MetricNotValid_ReturnsBadRequest() throws Exception {
        MetricResource metricResource = new MetricResource("");

        mockMvc.perform(post("/metric").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(metricResource))).andExpect(status().isBadRequest());
    }

    @Test
    void getAll_MetricsFound_ReturnsMetrics() throws Exception {
        when(metricService.getAll()).thenReturn(createMetrics(TEMPERATURE_TYPE, HUMIDITY_TYPE));

        MvcResult mvcResult = mockMvc.perform(get("/metric")).andExpect(status().isOk()).andReturn();

        List<Metric> metrics = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, Metric.class));
        assertThat(metrics).hasSize(2);
    }

    @Test
    void getAll_NoMetricsFound_ReturnsEmptyBody() throws Exception {
        when(metricService.getAll()).thenReturn(Collections.emptyList());

        MvcResult mvcResult = mockMvc.perform(get("/metric"))
                .andExpect(status().isOk())
                .andReturn();

        List<Metric> metrics = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, Metric.class));
        assertThat(metrics).isEmpty();
    }

    @Test
    void getById_MetricFound_ReturnsMetric() throws Exception {
        when(metricService.getById(1L)).thenReturn(Optional.of(new Metric(1L, TEMPERATURE_TYPE)));

        MvcResult mvcResult = mockMvc.perform(get("/metric/{id}", 1L)).andExpect(status().isOk()).andReturn();

        Metric metric = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Metric.class);
        assertNotNull(metric);
        assertEquals(1L, metric.getId());
    }

    @Test
    void getById_MetricNotFound_Returns404() throws Exception {
        when(metricService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/metric/{id}", 1L)).andExpect(status().isNotFound()).andExpect(content().string(StringUtils.EMPTY));
    }

    @Test
    void deleteById_MetricDeleted_Returns204() throws Exception {
        mockMvc.perform(delete("/metric/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(StringUtils.EMPTY));
    }

    private List<Metric> createMetrics(String... types) {
        List<Metric> metrics = new ArrayList<>();
        Arrays.asList(types).forEach(t -> metrics.add(new Metric(t)));
        return metrics;
    }

}
