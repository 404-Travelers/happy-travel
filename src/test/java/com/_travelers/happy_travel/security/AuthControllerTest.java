package com._travelers.happy_travel.security;

import com._travelers.happy_travel.security.jwt.JwtResponse;
import com._travelers.happy_travel.security.jwt.JwtService;
import com._travelers.happy_travel.users.Role;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserLoginRequest;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to convert object to JSON string for testing", exception);
        }
    }

    @Nested
    @DisplayName("REGISTER USER-POST /register")
    class RegisterUserTests {

        @Test
        @DisplayName("Should register a user and return 201 Created")
        void testRegisterUser() throws Exception {
            UserRegisterRequest request = new UserRegisterRequest("john", "john@gmail.com", "pasL09?sword");
            UserResponse userResponse = new UserResponse(1L, "john", "john@gmail.com", "ROLE_USER");

            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(asJsonString(List.of(userResponse))));

            verify(userService, times(1)).addUser(request);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when password is missing during registration")
        void testRegisterWithMissingPassword () throws Exception {
            UserRegisterRequest invalidRequest = new UserRegisterRequest("john", "john@gmail.com", "");

            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when username is missing during registration")
        void testRegisterWithMissingUsername() throws Exception {
            UserRegisterRequest invalidRequest = new UserRegisterRequest("", "john@gmail.com", "password");

            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when register request body is empty")
        void testRegisterWithEmptyBody () throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("LOGIN-POST /login")
    class LoginTests {

        @Test
        @DisplayName("Should login and return JWT token and HTTP 200")
        void testLogin() throws Exception {
            UserLoginRequest loginRequest = new UserLoginRequest("john", "password");
            JwtResponse expectedResponse = new JwtResponse("token-testing");
            String expectedResult = objectMapper.writeValueAsString(expectedResponse);
            given(jwtService.loginAuthentication(loginRequest)).willReturn(expectedResponse);

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResult));

            verify(jwtService, times(1)).loginAuthentication(loginRequest);
        }


        @Test
        @DisplayName("Should return 400 Bad Request when password is missing during login")
        void testLoginWithMissingPassword () throws Exception {
            UserLoginRequest invalidLogin = new UserLoginRequest("john", "");

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidLogin)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 401 Unauthorized when credentials are invalid")
        void testLoginWithInvalidCredentials () throws Exception {
            UserLoginRequest invalidLogin = new UserLoginRequest("nonexistent", "wrongpassword");
            given(jwtService.loginAuthentication(invalidLogin)).willThrow(new BadCredentialsException(""));

            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidLogin)))
                    .andExpect(status().isUnauthorized());
        }
    }
}
