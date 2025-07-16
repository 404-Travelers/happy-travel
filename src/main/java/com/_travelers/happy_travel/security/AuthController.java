package com._travelers.happy_travel.security;

import com._travelers.happy_travel.security.jwt.JwtResponse;
import com._travelers.happy_travel.security.jwt.JwtService;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserLoginRequest;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        //Put into the service
        //UserRegisterRequest userRequestWithRolByDefault = new UserRegisterRequest(userRegisterRequest.username(), userRegisterRequest.password(), "ROLE_USER");

        UserResponse userResponse = userService.addUser(userRegisterRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        JwtResponse jwtResponse = jwtService.loginAuthentication(userLoginRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
