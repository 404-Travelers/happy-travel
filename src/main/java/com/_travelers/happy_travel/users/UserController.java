package com._travelers.happy_travel.users;

import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//
//    @Operation(
//           summary = "Resister a new user",
//           description = "Creates a new user with default role 'USER'. Requires valid username, email, and password."
//            )
//    @PostMapping("/register")
//    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
//        UserResponse createdUser = userService.addUser(userRegisterRequest);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by ID. Throws error if not found."
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @Operation(
        summary = "Delete user by ID",
        description = "Deletes user with given ID. Returns 204 if successful."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update user by ID",
            description = "Updates an existing user’s username, email, or password."
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRegisterRequest userRequest
    ) {
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }
}
