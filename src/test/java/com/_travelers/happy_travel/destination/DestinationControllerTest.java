package com._travelers.happy_travel.destination;

import com._travelers.happy_travel.destinations.Destination;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;
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
    private UserDetails userDetailsNotOwner;
    private DestinationRequest destinationRequest;
    private DestinationRequest invalidDestinationRequest;
    private DestinationResponse destinationResponse;
    private DestinationResponseShort destinationResponseShort;
    private UserResponseShort userResponseShort;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ADMIN, new ArrayList<>());
        User testUser = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ADMIN, new ArrayList<>());
        testUserDetails = new CustomUserDetail(user);
        userDetailsNotOwner = new CustomUserDetail(testUser);
        invalidDestinationRequest = new DestinationRequest("Sp", null, "Nice", "image.jpg");
        userResponseShort = new UserResponseShort("Kate");
        destinationRequest = new DestinationRequest("Italy", "Rome", "Nice", "https://image.jpg");
        destinationResponse = new DestinationResponse(1L, "Italy", "Rome", "Nice", "https://image.jpg", userResponseShort);
        destinationResponseShort = new DestinationResponseShort(1L, "Italy", "Rome", "https://image.jpg", userResponseShort);
    }

    @AfterEach
    void afterTest() {
        verifyNoMoreInteractions(destinationService);
    }

    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    private static final Map<HttpMethod, Function<String, MockHttpServletRequestBuilder>> REQUEST_BUILDERS = Map.of(
            HttpMethod.GET, url -> get(url),
            HttpMethod.POST, url -> post(url),
            HttpMethod.PUT, url -> put(url),
            HttpMethod.DELETE, url -> delete(url)
    );

    private ResultActions performRequest(HttpMethod method, String url, Object body, UserDetails user) throws Exception {

        Function<String, MockHttpServletRequestBuilder> builderFunction = REQUEST_BUILDERS.get(method);

        if (builderFunction == null) {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        MockHttpServletRequestBuilder builder = builderFunction.apply(url);
        builder.contentType(MediaType.APPLICATION_JSON);


        if (body != null) {
            builder.content(asJsonString(body));
        }

        if (user != null) {
            builder.with(user(user));
        }

        return mockMvc.perform(builder);
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

            mockMvc.perform(get("/destinations/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(destinationResponse)));

            verify(destinationService, times(1)).getDestinationById(eq(id));
        }

        @Test
        void getDestinationById_whenDestinationDoesNotExist_returnsException() throws Exception {
            Long id = 999L;
            String expectedMessage = "Destination with id " + id + " not found";
            given(destinationService.getDestinationById(id)).willThrow(new EntityNotFoundException("Destination", "id", String.valueOf(id)));

            mockMvc.perform(get("/destinations/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.path").value("/destinations/" + id))
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

            performRequest(POST, "/destinations", destinationRequest, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(destinationResponse)));

            verify(destinationService, times(1)).addDestination(eq(destinationRequest), eq(user));
        }

        @Test
        void addDestination_whenRequestIsInvalid_returns400() throws Exception {
            performRequest(POST, "/destinations", invalidDestinationRequest, testUserDetails)
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
            performRequest(POST, "/destinations", invalidDestinationRequest, null)
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
        void updateDestination_whenRequestIsValid_returnsUpdatedDestination() throws Exception {
            Long id = 1L;
            given(destinationService.updateDestination(eq(id), eq(destinationRequest), any(User.class))).willReturn(destinationResponse);

            performRequest(PUT, "/destinations/" + id, destinationRequest, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(destinationResponse)));

            verify(destinationService, times(1)).updateDestination(eq(id), eq(destinationRequest), any(User.class));
        }

        @Test
        void updateDestination_whenRequestIsInvalid_returns400() throws Exception {
            performRequest(PUT, "/destinations/20", invalidDestinationRequest, testUserDetails)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.path").value("/destinations/20"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message.country").value("Country must be more than 3 characters and less than 50 characters"))
                    .andExpect(jsonPath("$.message.city").value("City is required"))
                    .andExpect(jsonPath("$.message.imageUrl").value("Invalid content type"));
        }

        @Test
        void updateDestination_whenUnauthorized_returns401() throws Exception {
            Long id = 1L;
            performRequest(PUT, "/destinations/" + id, destinationRequest, null)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/destinations/1"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }

        @Test
        void updateDestination_whenUserNotOwner_returns403() throws Exception {
            Long id = 1L;
            given(destinationService.updateDestination(eq(id), eq(destinationRequest), eq(user))).willThrow(new AccessDeniedException("You are not authorized to modify or delete this destination."));

            performRequest(PUT, "/destinations/" + id, destinationRequest, userDetailsNotOwner)
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.path").value("/destinations/1"))
                    .andExpect(jsonPath("$.status").value(403))
                    .andExpect(jsonPath("$.message").value("Forbidden: You are not authorized to modify or delete this destination."));

            verify(destinationService, times(1)).updateDestination(eq(id), eq(destinationRequest), any(User.class));
        }
    }

    @Nested
    @DisplayName("DELETE /destinations/id")
    class DeleteDestinationTests {

        @Test
        void deleteDestination_whenDestinationExists_returnsMessage() throws Exception {
            Long id = 1L;
            String message = "Destination with id " + id + " deleted successfully";
            given(destinationService.deleteDestination(id, user)).willReturn(message);

            performRequest(DELETE, "/destinations/" + id, destinationRequest, testUserDetails)
                    .andExpect(status().isOk())
                    .andExpect(content().string(message));

            verify(destinationService, times(1)).deleteDestination(id, user);
        }

        @Test
        void deleteDestination_whenDestinationDoesNotExist_returns404() throws Exception {
            Long id = 1L;
            String expectedMessage = "Destination with id " + id + " not found";
            given(destinationService.deleteDestination(id, user)).willThrow(new EntityNotFoundException(Destination.class.getSimpleName(), "id", id.toString()));

            performRequest(DELETE, "/destinations/" + id, destinationRequest, testUserDetails)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.path").value("/destinations/" + id))
                    .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                    .andExpect(jsonPath("$.message").value(expectedMessage));

            verify(destinationService, times(1)).deleteDestination(id, user);
        }

        @Test
        void deleteDestination_whenUnauthorized_returns401() throws Exception {
            Long id = 1L;

            performRequest(DELETE, "/destinations/" + id, destinationRequest, null)
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/destinations/1"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }

        @Test
        void deleteDestination_whenUserNotOwner_returns403() throws Exception {
            Long id = 1L;
            given(destinationService.deleteDestination(eq(id), any(User.class))).willThrow(new AccessDeniedException("You are not authorized to modify or delete this destination."));

            performRequest(DELETE, "/destinations/" + id, null, userDetailsNotOwner)
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.path").value("/destinations/1"))
                    .andExpect(jsonPath("$.status").value(403))
                    .andExpect(jsonPath("$.message").value("Forbidden: You are not authorized to modify or delete this destination."));

            verify(destinationService, times(1)).deleteDestination(eq(id), any(User.class));
        }
    }
}
