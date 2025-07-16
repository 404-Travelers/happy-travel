package com._travelers.happy_travel.destination;

import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(destinationService);
    }

    @Test
    void getAllDestinations_whenRequestIsValid_returnsListOfDestinationsResponseShortEntity() throws Exception {
        List<DestinationResponseShort> serviceResult = List.of(new DestinationResponseShort());
        String expectedJson = objectMapper.writeValueAsString(List.of(new DestinationResponseShort()));
        given(destinationService.getAllDestinations()).willReturn(serviceResult);

        mockMvc.perform(get("/destinations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(destinationService, times(1)).getAllDestinations();
    }

    @Test
    void getDestinationById_whenRequestIsValid_returnsDestinationResponseEntity() throws Exception {
        Long id = 1L;
        DestinationResponse serviceResult = new DestinationResponse();
        String expectedJson = objectMapper.writeValueAsString(new DestinationResponse());
        given(destinationService.getDestinationById(eq(id))).willReturn(serviceResult);

        mockMvc.perform(get("/destinations/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(destinationService, times(1)).getDestinationById(eq(id));
    }

    @Test
    void getDestinationById_whenRequestIsInvalid_returnsException() throws Exception {
        Long id = -7L;
        String message = "Destination id must be a positive number";
        String expectedJson = new ObjectMapper().writeValueAsString(
                new HashMap<String, String>() {{put("city", message);}}
        );

        mockMvc.perform(get("/destinations/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getMessage(), expectedJson))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void addDestination_whenRequestIsValid_returnsDestinationResponseEntity() throws Exception {
        DestinationRequest destinationRequest = new DestinationRequest();
        String jsonRequest = objectMapper.writeValueAsString(new DestinationRequest());
        DestinationResponse serviceResult = new DestinationResponse();
        String expectedJson = objectMapper.writeValueAsString(new DestinationResponse());
        given(destinationService.addDestination(eq(destinationRequest))).willReturn(serviceResult);

        mockMvc.perform(post("/destinations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(destinationService, times(1)).addDestination(eq(destinationRequest));
    }

    @Test
    void addDestination_whenRequestIsInvalid_returnsException() throws Exception {
        String jsonInvalidRequest = objectMapper.writeValueAsString(new DestinationRequest());
        String message = "Destination city must contain min 2 and max 50 characters";
        String expectedJson = new ObjectMapper().writeValueAsString(
                new HashMap<String, String>() {{put("city", message);}}
        );

        mockMvc.perform(post("/destinations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInvalidRequest))
                .andExpect(status().isConflict())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void updateDestination_whenRequestIsValid_returnsDestinationResponseEntity() throws Exception {
        DestinationRequest destinationRequest = new DestinationRequest();
        String jsonRequest = objectMapper.writeValueAsString(new DestinationRequest());
        DestinationResponse serviceResult = new DestinationResponse();
        String expectedJson = objectMapper.writeValueAsString(new DestinationResponse());
        given(destinationService.updateDestination(eq(destinationRequest))).willReturn(serviceResult);

        mockMvc.perform(put("/destinations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(destinationService, times(1)).updateDestination(eq(destinationRequest));
    }

    @Test
    void updateDestination_whenRequestIsInvalid_returnsException() throws Exception {
        String jsonInvalidRequest = objectMapper.writeValueAsString(new DestinationRequest());
        String message = "Destination city must contain min 2 and max 50 characters";
        String expectedJson = new ObjectMapper().writeValueAsString(
                new HashMap<String, String>() {{put("city", message);}}
        );

        mockMvc.perform(put("/destinations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonInvalidRequest))
                .andExpect(status().isConflict())
                .andExpect(content().json(expectedJson));
    }

        @Test
    void deleteDestination_whenRequestIsValid_returnsMessageEntity() throws Exception{
        Long id = 1L;
        String message = "Destination deleted successfully";
        given(destinationService.deleteDestination(eq(id))).willReturn(message);

        mockMvc.perform(delete("/destinations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Destination deleted successfully"));

        verify(destinationService, times(1)).deleteDestination(eq(id));
    }
}