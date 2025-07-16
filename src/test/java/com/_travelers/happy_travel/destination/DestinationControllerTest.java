package com._travelers.happy_travel.destination;

import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DestinationControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DestinationService destinationService;

    @Autowired
    private ObjectMapper objectMapper;


    private DestinationResponseShort destinationResponseShort;
    private String jsonDestinationRequest;
    private DestinationResponse destinationResponse;
    private DestinationRequest destinationRequest;
    private Long id;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllDestinations_whenDestinationsExist_returnsListOfDestinationsResponseShort() throws JsonProcessingException {
        List<DestinationResponseShort> serviceResult = List.of(new DestinationResponseShort());
        String expectedJson = objectMapper.writeValueAsString(List.of(new DestinationResponseShort()));
        given(destinationService.getAllDestinations()).willReturn(serviceResult);

        mockMvc.perform(get("/destinations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}