package com._travelers.happy_travel.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest {

    @Nested
    @DisplayName("GET /users")
    class GetUsersTests {

//
//        @Test
//        void getAllUsers_whenRequestIsValid_returnsListOfUsersResponseShortEntity() throws Exception {
//            String expectedJson = objectMapper.writeValueAsString(List.of(userResponse));
//            given(userService.getAllUsers()).willReturn(List.of(userResponse));
//
//            mockMvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().json(expectedJson));
//
//            verify(userService, times(1)).getAllUsers();
//        }

//        @Test
//        void geMyUser_whenRequestIsValid_returnsUserResponseEntity() throws Exception {
//            Long id = 1L;
//            String expectedJson = objectMapper.writeValueAsString(userResponse);
//            given(userService.getOwnUser(eq(id))).willReturn(userResponse);
//
//            mockMvc.perform(get("/users/{id}", id)
//                            .with(user(testUserDetails))
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().json(expectedJson));
//
//            verify(userService, times(1)).getOwnUser(eq(id));
//        }
//
//        @Test
//        void getMyUser_whenRequestIsInvalid_returnsException() throws Exception {
//            Long id = -7L;
//            String message = "User id must be a positive number";
//            String expectedJson = new ObjectMapper().writeValueAsString(
//                    new HashMap<String, String>() {{
//                        put("city", message);
//                    }}
//            );
//
//            mockMvc.perform(get("/users/{id}", id))
//                    .andExpect(status().isConflict())
//                    .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getMessage(), expectedJson))
//                    .andExpect(content().json(expectedJson));
//        }
    }
//    @Nested
//    @DisplayName("POST /users")
//    class AddUsersTests {
//
//        @Test
//        void addUser_whenRequestIsValid_returnsUserResponseEntity() throws Exception {
//            String jsonRequest = objectMapper.writeValueAsString(userRegisterRequest);
//            String expectedJson = objectMapper.writeValueAsString(userResponse);
//            given(userService.addUser(eq(userRegisterRequest))).willReturn(userResponse);
//
//            mockMvc.perform(post("/users")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(jsonRequest))
//                    .andExpect(status().isOk())
//                    .andExpect(content().json(expectedJson));
//
//            verify(userService, times(1)).addUser(eq(userRegisterRequest));
//        }
//
//        @Test
//        void addUser_whenRequestIsInvalid_returnsException() throws Exception {
//            String jsonInvalidRequest = objectMapper.writeValueAsString(userRegisterRequest);
//            String message = "Username must contain min 2 and max 50 characters";
//            String expectedJson = new ObjectMapper().writeValueAsString(
//                    new HashMap<String, String>() {{
//                        put("username", message);
//                    }}
//            );
//
//            mockMvc.perform(post("/users")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(jsonInvalidRequest))
//                    .andExpect(status().isConflict())
//                    .andExpect(content().json(expectedJson));
//        }
//    }
}
