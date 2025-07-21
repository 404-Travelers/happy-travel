package com._travelers.happy_travel.users;

import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserResponse;
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

//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest{
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void afterTest(){
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    void getAllUsers_whenRequestIsValid_returnsListOfUsersResponseShortEntity() throws Exception {
//        List<UserResponse> serviceResult = List.of(new UserResponse());
//        String expectedJson = objectMapper.writeValueAsString(List.of(new UserResponse()));
//        given(userService.getAllUsers()).willReturn(serviceResult);
//
//        mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    void getUserById_whenRequestIsValid_returnsUserResponseEntity() throws Exception {
//        Long id = 1L;
//        UserResponse serviceResult = new UserResponse();
//        String expectedJson = objectMapper.writeValueAsString(new UserResponse());
//        given(userService.getUserById(eq(id))).willReturn(serviceResult);
//
//        mockMvc.perform(get("/users/{id}", id)
//                    .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//
//        verify(userService, times(1)).getUserById(eq(id));
//    }
//
//    @Test
//    void getUserById_whenRequestIsInvalid_returnsException() throws Exception {
//        Long id = -7L;
//        String message = "User id must be a positive number";
//        String expectedJson = new ObjectMapper().writeValueAsString(
//                new HashMap<String, String>() {{put("city", message);}}
//        );
//
//        mockMvc.perform(get("/users/{id}", id))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getMessage(), expectedJson))
//                .andExpect(content().json(expectedJson));
//    }
//// Register and Login
////    @Test
////    void addUser_whenRequestIsValid_returnsUserResponseEntity() throws Exception {
////        UserRequest userRequest = new UserRequest();
////        String jsonRequest = objectMapper.writeValueAsString(new UserRequest());
////        UserResponse serviceResult = new UserResponse();
////        String expectedJson = objectMapper.writeValueAsString(new UserResponse());
////        given(userService.addUser(eq(userRequest))).willReturn(serviceResult);
////
////        mockMvc.perform(post("/users")
////                    .contentType(MediaType.APPLICATION_JSON)
////                    .content(jsonRequest))
////                .andExpect(status().isOk())
////                .andExpect(content().json(expectedJson));
////
////        verify(userService, times(1)).addUser(eq(userRequest));
////    }
////
////    @Test
////    void addUser_whenRequestIsInvalid_returnsException() throws Exception {
////        String jsonInvalidRequest = objectMapper.writeValueAsString(new UserRequest());
////        String message = "Username must contain min 2 and max 50 characters";
////        String expectedJson = new ObjectMapper().writeValueAsString(
////                new HashMap<String, String>() {{put("username", message);}}
////        );
////
////        mockMvc.perform(post("/users")
////                    .contentType(MediaType.APPLICATION_JSON)
////                    .content(jsonInvalidRequest))
////                .andExpect(status().isConflict())
////                .andExpect(content().json(expectedJson));
////    }
//
//    @Test
//    void updateUser_whenRequestIsValid_returnsUserResponseEntity() throws Exception {
//        Long id = 1L;
//        UserRequest userRequest = new UserRequest();
//        String jsonRequest = objectMapper.writeValueAsString(new UserRequest());
//        UserResponse serviceResult = new UserResponse();
//        String expectedJson = objectMapper.writeValueAsString(new UserResponse());
//        given(userService.updateUser(eq(userRequest))).willReturn(serviceResult);
//
//        mockMvc.perform(put("/users/{id}", id)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(jsonRequest))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson));
//
//        verify(userService, times(1)).updateUser(eq(userRequest));
//    }
//
//    @Test
//    void updateUser_whenRequestIsInvalid_returnsException() throws Exception {
//        Long id = 1L;
//        String jsonInvalidRequest = objectMapper.writeValueAsString(new UserRequest());
//        String message = "Username must contain min 2 and max 50 characters";
//        String expectedJson = new ObjectMapper().writeValueAsString(
//                new HashMap<String, String>() {{put("username", message);}}
//        );
//
//        mockMvc.perform(put("/users/{id}", id)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(jsonInvalidRequest))
//                .andExpect(status().isConflict())
//                .andExpect(content().json(expectedJson));
//    }
//
//    @Test
//    void deleteUser_whenRequestIsValid_returnsMessageEntity() throws Exception{
//        Long id = 1L;
//        String message = "User deleted successfully";
//        given(userService.deleteUser(eq(id))).willReturn(message);
//
//        mockMvc.perform(delete("/users/{id}", id))
//                .andExpect(status().isOk())
//                .andExpect(content().string("User deleted successfully"));
//
//        verify(userService, times(1)).deleteUser(eq(id));
//    }
//
//    @Test
//    void deleteUser_whenRequestIsInvalid_returnsException() throws Exception {
//        Long invalidId = -7L;
//        String message = "User id must be a positive number";
//        String expectedJson = new ObjectMapper().writeValueAsString(
//               message
//        );
//
//        mockMvc.perform(delete("/users/{id}", invalidId))
//                .andExpect(status().isConflict())
//                .andExpect(content().json(expectedJson));
//    }
//}
