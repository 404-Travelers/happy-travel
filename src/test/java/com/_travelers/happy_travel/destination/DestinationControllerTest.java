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
import com._travelers.happy_travel.users.dto.UserResponseShort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

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
    private DestinationRequest invalidDestinationRequest;
    private DestinationResponse destinationResponse;
    private DestinationResponseShort destinationResponseShort;
    private UserResponseShort userResponseShort;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ADMIN, new ArrayList<>());
        testUserDetails = new CustomUserDetail(user);
        invalidDestinationRequest = new DestinationRequest("Sp", null, "Nice", "image.jpg");
        userResponseShort = new UserResponseShort("Kate");
        destinationRequest = new DestinationRequest("Spain", "Valencia", "Nice", "https://image.jpg");
        destinationResponse = new DestinationResponse("Spain", "Valencia", "Nice", "https://image.jpg", userResponseShort);
        destinationResponseShort = new DestinationResponseShort("Spain", "Valencia", "https://image.jpg", userResponseShort);
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(destinationService);
    }

    private String asJsonString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    private ResultActions performPostRequest(String url, Object body, UserDetails user) throws Exception {
        return mockMvc.perform(post(url)
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body)));
    }

    private ResultActions performPostRequestWithoutUser(String url, Object body) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body)));
    }

    @Nested
    @DisplayName("GET /destinations")
    class GetDestinationsTests {

        @Test
        void getAllDestinations_returnsDestinationsList() throws Exception {
            given(destinationService.getAllDestinations()).willReturn(List.of(destinationResponseShort));

            mockMvc.perform(get("/destinations"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of(destinationResponseShort))));

            verify(destinationService, times(1)).getAllDestinations();
        }

        @Test
        void getAllDestinations_returnsEmptyList() throws Exception {
            given(destinationService.getAllDestinations()).willReturn(List.of());

            mockMvc.perform(get("/destinations"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of())));

            verify(destinationService, times(1)).getAllDestinations();
        }

        @Test
        void getDestinationById_whenDestinationExists_returnsDestination() throws Exception {
            Long id = 1L;
            given(destinationService.getDestinationById(id)).willReturn(destinationResponse);

            mockMvc.perform(get("/destinations/id/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(destinationResponse)));

            verify(destinationService, times(1)).getDestinationById(eq(id));
        }

        @Test
        void getDestinationById_whenDestinationDoesNotExist_returnsException() throws Exception {
            Long id = 999L;
            String expectedMessage = "Destination with id " + id + " not found";
            given(destinationService.getDestinationById(id)).willThrow(new EntityNotFoundException("Destination", "id", String.valueOf(id)));

            mockMvc.perform(get("/destinations/id/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.path").value("/destinations/id/" + id))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.message").value(expectedMessage));

            verify(destinationService, times(1)).getDestinationById(eq(id));
        }


        @Test
        void getDestinationsByUserUsername_whenUserExists_returnsDestinationsList() throws Exception {
            given(destinationService.getDestinationsByUserUsername(user.getUsername())).willReturn(List.of(destinationResponse));

            mockMvc.perform(get("/destinations/user?username={username}", user.getUsername()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of(destinationResponse))));

            verify(destinationService, times(1)).getDestinationsByUserUsername(user.getUsername());
        }

        @Test
        void getDestinationsByUserUsername_whenUserDoesNotExist_returnsException() throws Exception {
            String username = "No user";
            String expectedMessage = "User with username " + username + " not found";
            given(destinationService.getDestinationsByUserUsername(username)).willThrow(new EntityNotFoundException("User", "username", username));

            mockMvc.perform(get("/destinations/user?username={username}", username))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.path").value("/destinations/user"))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.message").value(expectedMessage));

            verify(destinationService, times(1)).getDestinationsByUserUsername(username);
        }
    }

    @Nested
    @DisplayName("POST /destinations")
    class AddDestinationTests {

        @Test
        void addDestination_whenRequestIsValid_returnsCreatedDestination() throws Exception {
            given(destinationService.addDestination(eq(destinationRequest), eq(user)))
                    .willReturn(destinationResponse);

            performPostRequest("/destinations", destinationRequest, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(destinationResponse)));

            verify(destinationService, times(1)).addDestination(eq(destinationRequest), eq(user));
        }

        @Test
        void addDestination_whenRequestIsInvalid_returns400() throws Exception {
            performPostRequest("/destinations", invalidDestinationRequest, testUserDetails)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.path").value("/destinations"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message.country").value("Country must be more than 3 characters and less than 50 characters"))
                    .andExpect(jsonPath("$.message.city").value("City is required"))
                    .andExpect(jsonPath("$.message.imageUrl").value("Invalid content type"));
        }

        @Test
        void addDestination_withoutAuthentication_returns401() throws Exception {
            performPostRequestWithoutUser("/destinations", invalidDestinationRequest)
                    .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.path").value("/destinations"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }

    @Nested
    @DisplayName("PUT /destinations/id")
    class UpdateDestinationTests {

        @Test
        void updateDestination_returnsUpdated() throws Exception {
            Long id = 1L;
            given(destinationService.updateDestination(eq(id), eq(destinationRequest), any(User.class)))
                    .willReturn(destinationResponse);

            mockMvc.perform(put("/destinations/user/{id}", id)
                            .with(user(testUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(destinationRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(destinationResponse)));

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
                            .content(asJsonString(destinationRequest)))
                    .andExpect(status().isForbidden());
            // and expect content

            verify(destinationService, times(1)).updateDestination(eq(id), eq(destinationRequest), eq(user));
        }
    }

    @Nested
    @DisplayName("DELETE /destinations/id")
    class DeleteDestinationTests {

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
}
