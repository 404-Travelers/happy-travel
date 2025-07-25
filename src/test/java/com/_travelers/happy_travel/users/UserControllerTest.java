package com._travelers.happy_travel.users;

import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.exceptions.ErrorResponse;
import com._travelers.happy_travel.security.CustomUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegisterRequest userRegisterRequest;
    private UserRegisterRequest invalidUserRegisterRequest;
    private UserResponse userResponse;
    private UserDetails testUserDetails;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ADMIN, new ArrayList<>());
        testUserDetails = new CustomUserDetail(user);
        userRegisterRequest = new UserRegisterRequest("Katie", "kate.dev@gmail.com", "mypass1234*");
        userResponse = new UserResponse("Katie", "kate.dev@gmail.com", "ROLE_USER");
        invalidUserRegisterRequest = new UserRegisterRequest("Kate", "kate.dev@gmail.com", "mypass1234*");
        mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(userService);
    }

    @Nested
    @DisplayName("GET /users")
    class GetUsersTests {

        @Test
        void geMyUser_whenRequestIsValid_returnsUser() throws Exception {
            Long id = user.getId();
            String expectedJson = objectMapper.writeValueAsString(userResponse);
            given(userService.getOwnUser(eq(id))).willReturn(userResponse);

            mockMvc.perform(get("/users/me")
                            .with(user(testUserDetails)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));

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
    }

    @Nested
    @DisplayName("PUT /users")
    class UpdateUsersTests {

        @Test
        void updateUser_whenRequestIsValid_returnsUserResponseEntity() throws Exception {
            Long id = 1L;
            String jsonRequest = objectMapper.writeValueAsString(userRegisterRequest);
            String expectedJson = objectMapper.writeValueAsString(userResponse);
            given(userService.updateOwnUser(eq(id), eq(userRegisterRequest))).willReturn(userResponse);

            mockMvc.perform(put("/users/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));

            verify(userService, times(1)).updateOwnUser(eq(id), eq(userRegisterRequest));
        }

        @Test
        void updateUser_whenInvalidUsername_returnsBadRequest() throws Exception {
            Map<String, String> errors = new HashMap<>();
            errors.put("username", "Username must be between 3 and 50 characters");
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .error("VALIDATION_ERROR")
                    .path("/users/1")
                    .timestamp(LocalDateTime.now())
                    .message(errors)
                    .status(HttpStatus.BAD_REQUEST.value()).build();
            String jsonRequest = objectMapper.writeValueAsString(invalidUserRegisterRequest);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            mockMvc.perform(put("/users/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.path").value("/users/1"))
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message.username").value("Username must be between 3 and 50 characters"));
        }
    }

    @Nested
    @DisplayName("DELETE /users")
    class DeleteUsersTests {


        @Test
        void deleteUser_whenRequestIsValid_returnsMessageEntity() throws Exception {
            Long id = 1L;
            String message = "User deleted successfully";
            given(userService.deleteOwnUser(eq(id))).willReturn(message);

            mockMvc.perform(delete("/users/{id}", id))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("User deleted successfully"));

            verify(userService, times(1)).deleteOwnUser(eq(id));
        }

        @Test
        void deleteUser_whenRequestIsInvalid_returnsException() throws Exception {
            Long invalidId = -7L;
            String message = "User id must be a positive number";
//        String expectedJson = new ObjectMapper().writeValueAsString(
//               message
//        );

            mockMvc.perform(delete("/users/{id}", invalidId))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(message));
        }
    }
}
