package com._travelers.happy_travel.users;

import com._travelers.happy_travel.security.CustomUserDetail;
import com._travelers.happy_travel.users.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "Returns all users. Throws error if not found."
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by ID. Throws error if not found."
    )
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable @Positive(message = "User id must be a positive number") Long id) {
        UserResponse user = userService.getUserByIdAdmin(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Deletes user with given ID. Returns 204 if successful."
    )
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable @Positive(message = "User id must be a positive number") Long id) {
        String message = userService.deleteUserByIdAdmin(id);
        return ResponseEntity.ok(message);
    }
}
