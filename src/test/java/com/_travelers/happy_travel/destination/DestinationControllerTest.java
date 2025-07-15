package com._travelers.happy_travel.destination;

import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    private DestinationResponse destinationResponse1;
    private DestinationResponse destinationResponse2;
    private DestinationRequest destinationRequest;
    private Long id;

    @BeforeEach(){
    }


}