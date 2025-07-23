package com._travelers.happy_travel.users;

import com._travelers.happy_travel.security.CustomUserDetail;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by ID. Throws error if not found."
    )
    @GetMapping("")
    public ResponseEntity<UserResponse> getOwnUser(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        UserResponse user = userService.getOwnUser(userDetail.getId());
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Update user by ID",
            description = "Updates an existing user’s username, email, or password."
    )
    @PutMapping("")
    public ResponseEntity<UserResponse> updateOwnUser(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid UserRegisterRequest userRequest
    ) {
        UserResponse updatedUser = userService.updateOwnUser(userDetail.getId(), userRequest);
        return ResponseEntity.ok(updatedUser);
    }


    @Operation(
            summary = "Delete user by ID",
            description = "Deletes user with given ID. Returns 204 if successful."
    )
    @DeleteMapping("")
    public ResponseEntity<String> deleteOwnUser(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        String message = userService.deleteOwnUser(userDetail.getId());
        return ResponseEntity.ok(message);
    }
}
