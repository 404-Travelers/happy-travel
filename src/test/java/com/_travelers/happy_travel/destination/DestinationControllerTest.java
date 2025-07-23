package com._travelers.happy_travel.destination;

import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.security.CustomUserDetail;
import com._travelers.happy_travel.users.Role;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class DestinationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DestinationService destinationService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDetails testUserDetails;
    private DestinationRequest destinationRequest;
    private DestinationResponse destinationResponse;
    private DestinationResponseShort destinationResponseShort;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ROLE_ADMIN, new ArrayList<>());
        testUserDetails = new CustomUserDetail(user); // ⚠️ implements UserDetails

        userResponse = new UserResponse("Kate", "kate.dev@gmail.com", "ROLE_USER");
        destinationRequest = new DestinationRequest("Spain", "Valencia", "Nice", "image.jpg");
        destinationResponse = new DestinationResponse("Spain", "Valencia", "Nice", "image.jpg", userResponse);
        destinationResponseShort = new DestinationResponseShort("Spain", "Valencia", "image.jpg", userResponse);
    }

    @Test
    void getAllDestinations_returnsListOfShortResponses() throws Exception {
        given(destinationService.getAllDestinations()).willReturn(List.of(destinationResponseShort));

        mockMvc.perform(get("/destinations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(destinationResponseShort))));

        verify(destinationService, times(1)).getAllDestinations();
    }

    @Test
    void getAllDestinations_returnsEmptyList() throws Exception {
        given(destinationService.getAllDestinations()).willReturn(List.of());

        mockMvc.perform(get("/destinations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));

        verify(destinationService, times(1)).getAllDestinations();
    }

    @Test
    void getDestinationById_returnsDestinationResponse() throws Exception {
        Long id = 1L;
        given(destinationService.getDestinationById(id)).willReturn(destinationResponse);

        mockMvc.perform(get("/destinations/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destinationResponse)));

        verify(destinationService, times(1)).getDestinationById(eq(id));
    }

    @Test
    void getDestinationById_destinationNotFound_returns404() throws Exception {
        Long id = 999L;
        given(destinationService.getDestinationById(id)).willThrow(new EntityNotFoundException("Destination", "id", String.valueOf(id)));

        mockMvc.perform(get("/destinations/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void getDestinationsByUserId_returnsUserDestinations() throws Exception {
        Long userId = 1L;
        given(destinationService.getDestinationsByUserId(user.getId())).willReturn(List.of(destinationResponse));

        mockMvc.perform(get("/destinations/user")
                        .with(user(testUserDetails))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(destinationResponse))));

        verify(destinationService, times(1)).getDestinationsByUserId(user.getId());
    }

    @Test
    void addDestination_returnsCreatedDestination() throws Exception {
        given(destinationService.addDestination(eq(destinationRequest), eq(user)))
                .willReturn(destinationResponse);

        mockMvc.perform(post("/destinations/user")
                        .with(user(testUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destinationResponse)));

        verify(destinationService, times(1)).addDestination(eq(destinationRequest), eq(user));
    }

    @Test
    void addDestination_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(post("/destinations/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinationRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateDestination_returnsUpdated() throws Exception {
        Long id = 1L;
        given(destinationService.updateDestination(eq(id), eq(destinationRequest), any(User.class)))
                .willReturn(destinationResponse);

        mockMvc.perform(put("/destinations/user/{id}", id)
                        .with(user(testUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(destinationResponse)));

        verify(destinationService, times(1)).updateDestination(eq(id), eq(destinationRequest), any(User.class));
    }

    @Test
    void updateDestination_whenUnauthorized_returns403() throws Exception {
        Long id = 1L;
        given(destinationService.updateDestination(eq(id), eq(destinationRequest), eq(user)))
                .willThrow(new AccessDeniedException("Forbidden")); // Simulating not allowed

        mockMvc.perform(put("/destinations/user/{id}", id)
                        .with(user(testUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(destinationRequest)))
                .andExpect(status().isForbidden());
        // and expect content

        verify(destinationService, times(1)).updateDestination(eq(id), eq(destinationRequest), eq(user));
    }

    @Test
    void deleteDestination_successful() throws Exception {
        Long id = 1L;
        String message = "Destination with id " + id + " deleted successfully";
        given(destinationService.deleteDestination(id, user)).willReturn(message);

        mockMvc.perform(delete("/destinations/user/{id}", id)
                        .with(user(testUserDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string(message))
                        .andReturn();

        verify(destinationService, times(1)).deleteDestination(id, user);
    }

    @Test
    void deleteDestination_unauthorized_returns403() throws Exception {
        Long id = 1L;
        given(destinationService.deleteDestination(id, user)).willThrow(new AccessDeniedException("Forbidden"));

        mockMvc.perform(delete("/destinations/user/{id}", id)
                        .with(user(testUserDetails)))
                .andExpect(status().isForbidden());
        //and expect json

        verify(destinationService, times(1)).deleteDestination(id, user);
    }
}
