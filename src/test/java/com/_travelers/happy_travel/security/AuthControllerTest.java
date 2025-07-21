package com._travelers.happy_travel.security;

import com._travelers.happy_travel.security.jwt.JwtResponse;
import com._travelers.happy_travel.security.jwt.JwtService;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserLoginRequest;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Loads the full context (service layer, repositories, etc.) [1][2][3][5][6]
@AutoConfigureMockMvc // Auto-configures and injects MockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

//    @Test
//    @DisplayName("Should register a user and return 201 Created")
//    void testRegisterUser() throws Exception {
//        UserRegisterRequest request = new UserRegisterRequest("john", "john@gmail.com", "password");
//        String expectedJson = objectMapper.writeValueAsString(List.of(new UserResponse("john", "john@gmail.com", "ROLE_USER")));
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(content().json(expectedJson));
//
//        verify(userService, times(1)).addUser(request);
//    }
//
//    @Test
//    @DisplayName("Should login and return JWT token and HTTP 200")
//    void testLogin() throws Exception {
//        UserLoginRequest loginRequest = new UserLoginRequest("john", "password");
//        String expectedResult = objectMapper.writeValueAsString(new JwtResponse("token-testing"));
//        given(jwtService.loginAuthentication(loginRequest)).willReturn(serviceResult);
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedResult));
//                // Add .andExpect() for specific JSON response fields if needed
//
//        verify(jwtService, times(1)).loginAuthentication(loginRequest);
//    }
//
//    // ✅ SAD PATH: Register with missing fields (Validation Failure)
//    @Test
//    @DisplayName("Should return 400 Bad Request when username is missing during registration")
//    void testRegisterWithMissingUsername() throws Exception {
//        UserRegisterRequest invalidRequest = new UserRegisterRequest("", "password", null);
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ✅ SAD PATH: Register with missing password
//    @Test
//    @DisplayName("Should return 400 Bad Request when password is missing during registration")
//    void testRegisterWithMissingPassword() throws Exception {
//        UserRegisterRequest invalidRequest = new UserRegisterRequest("john", "", null);
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ✅ SAD PATH: Login with missing password
//    @Test
//    @DisplayName("Should return 400 Bad Request when password is missing during login")
//    void testLoginWithMissingPassword() throws Exception {
//        UserLoginRequest invalidLogin = new UserLoginRequest("john", "");
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidLogin)))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ✅ SAD PATH: Login with user that doesn't exist or wrong credentials
//    @Test
//    @DisplayName("Should return 401 Unauthorized when credentials are invalid")
//    void testLoginWithInvalidCredentials() throws Exception {
//        UserLoginRequest invalidLogin = new UserLoginRequest("nonexistent", "wrongpassword");
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidLogin)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    // ✅ SAD PATH: Submit completely empty request body for register
//    @Test
//    @DisplayName("Should return 400 Bad Request when register request body is empty")
//    void testRegisterWithEmptyBody() throws Exception {
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isBadRequest());
//    }
}
