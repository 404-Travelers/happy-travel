package com._travelers.happy_travel.users;

import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import com._travelers.happy_travel.security.CustomUserDetail;
import com._travelers.happy_travel.users.dto.UserResponseShort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private DestinationService destinationService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegisterRequest userRegisterRequest;
    private UserRegisterRequest invalidUserRegisterRequest;
    private UserResponse userResponse;
    private CustomUserDetail testUserDetails;
    private User user;
    private DestinationResponse destinationResponse;

    @BeforeEach
    void setUp() {
        destinationResponse = new DestinationResponse("Spain", "Valencia","Nice", "image.jpg", new UserResponseShort("Kate"));
        user = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ADMIN, new ArrayList<>());
        testUserDetails = new CustomUserDetail(user);
        userRegisterRequest = new UserRegisterRequest("Katie", "kate.dev@gmail.com", "myPpas%s1234*");
        userResponse = new UserResponse("Katie", "kate.dev@gmail.com", "ROLE_USER");
        invalidUserRegisterRequest = new UserRegisterRequest("Ka", "kate.dev@gmail.com", "mypass1234*");
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(userService);
    }

    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    @Nested
    @DisplayName("GET /users")
    class GetUsersTests {

        @Test
        void geMyUser_withAuthentication_returnsUser() throws Exception {
            Long id = user.getId();
            given(userService.getOwnUser(eq(id))).willReturn(userResponse);

            mockMvc.perform(get("/users/me")
                            .with(user(testUserDetails)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(userResponse)));

            verify(userService, times(1)).getOwnUser(eq(id));
        }
        @Test
        void getMyUser_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(get("/users/me"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }

        @Test
        void geMyDestinations_withAuthentication_returnsDestinationsList() throws Exception {
            given(destinationService.getDestinationsByUserUsername(eq(testUserDetails.getUser().getUsername()))).willReturn(List.of(destinationResponse));

            mockMvc.perform(get("/users/me/destinations")
                            .with(user(testUserDetails)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(List.of(destinationResponse))));


            verify(destinationService, times (1)).getDestinationsByUserUsername(eq(testUserDetails.getUser().getUsername()));
        }

        @Test
        void getMyDestinations_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(get("/users/me/destinations"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me/destinations"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }

    @Nested
    @DisplayName("PUT /users")
    class UpdateUserTests {

        @Test
        void updateUser_withAuthentication_returnsUser() throws Exception {
            Long id = 1L;
            given(userService.updateOwnUser(eq(id), eq(userRegisterRequest))).willReturn(userResponse);

            mockMvc.perform(put("/users/me")
                            .with(user(testUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(userRegisterRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(asJsonString(userResponse)));

            verify(userService, times(1)).updateOwnUser(eq(id), eq(userRegisterRequest));
        }

        @Test
        void updateUser_whenInvalidUsernameAndPassword_returns400() throws Exception {

            mockMvc.perform(put("/users/me", 1)
                            .with(user(testUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(invalidUserRegisterRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.length()").value(5))
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message.length()").value(2))
                    .andExpect(jsonPath("$.message.username").value("Username must be between 3 and 50 characters"))
                    .andExpect(jsonPath("$.message.password").value("Password must contain a minimum of 8 characters and a max of 50 characters, including a number, one uppercase letter, one lowercase letter and one special character"));
        }

        @Test
        void updateUser_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(put("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegisterRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }

    @Nested
    @DisplayName("DELETE /users")
    class DeleteUsersTests {


        @Test
        void deleteUser_withAuthentication_returnsMessage() throws Exception {
            Long id = 1L;
            String message = "User deleted successfully";
            given(userService.deleteOwnUser(eq(id))).willReturn(message);

            mockMvc.perform(delete("/users/me")
                            .with(user(testUserDetails)))
                    .andExpect(content().string(message));

            verify(userService, times(1)).deleteOwnUser(eq(id));
        }

        @Test
        void deleteUser_withoutAuthentication_returns401() throws Exception {
            mockMvc.perform(delete("/users/me"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.length()").value(5))
                    .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.path").value("/users/me"))
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Unauthorized: Full authentication is required to access this resource"));
        }
    }
}
