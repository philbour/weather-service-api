package org.philbour.weatherservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

@WebMvcTest(controllers = QueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void metricAverage_ValidRequest_ResponseReturned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/query/metric/{type}", "temperature").queryParam("from", LocalDate
                .now()
                .toString()).queryParam("to", LocalDate.now().toString())).andExpect(status().isOk()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualTo("0");
    }

    @Test
    void metricAverage_ToDateInFuture_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/query/metric/{type}", "temperature").queryParam("from", LocalDate.now().toString())
                .queryParam("to", LocalDate.now().plusYears(1).toString())).andExpect(status().isBadRequest());
    }

}
