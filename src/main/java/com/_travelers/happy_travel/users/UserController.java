package com._travelers.happy_travel.users;

import com._travelers.happy_travel.common.SecuredBaseController;
import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
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

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends SecuredBaseController {

    private final UserService userService;
    private final DestinationService destinationService;

    @Operation(
            summary = "Get user by authentication",
            description = "Returns a user by authentication. Throws error if not found."
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        UserResponse user = userService.getOwnUser(userDetail.getId());
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Get destinations by authenticated user",
            description = "Return destinations by authenticated user. Throws error if not found."
    )
    @GetMapping("/me/destinations")
    public ResponseEntity<List<DestinationResponse>> getMyDestinations(@AuthenticationPrincipal CustomUserDetail userDetail) {
        List<DestinationResponse> list = destinationService.getDestinationsByUserUsername(userDetail.getUser().getUsername());
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "Update authenticated user",
            description = "Updates the authenticated user's username, email, or password."
    )
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid UserRegisterRequest userRequest
    ) {
        UserResponse updatedUser = userService.updateOwnUser(userDetail.getId(), userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete authenticated user",
            description = "Deletes authenticated user. Returns a message if successful."
    )
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyUser(
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        String message = userService.deleteOwnUser(userDetail.getId());
        return ResponseEntity.ok(message);
    }
}
