package com._travelers.happy_travel.destinations;


import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;
    private final UserService userService;

    public DestinationController(DestinationService destinationService, UserService userService) {
        this.destinationService = destinationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<DestinationResponseShort>> getAllDestinations() {
        List<DestinationResponseShort> list = destinationService.getAllDestinations()
                .stream()
                .map(dest -> new DestinationResponseShort(
                        dest.country(),
                        dest.city(),
                        dest.imageUrl(),
                        dest.user()
                ))
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponse> getDestinationById(@PathVariable Long id) {
        DestinationResponse destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DestinationResponse>> getDestinationsByUserId(@AuthenticationPrincipal User user) {
        List<DestinationResponse> list = destinationService.getDestinationByUserId(user.getId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DestinationResponse> addDestination(
            @AuthenticationPrincipal User user,
            @RequestBody DestinationRequest request) {
        DestinationResponse created = destinationService.addDestination(request, user);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DestinationResponse> updateDestination(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody DestinationRequest request) {
        DestinationResponse updated = destinationService.updateDestination(id, request, user);
        if (updated == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteDestination(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        String message = destinationService.deleteDestination(id, user);
        if (message == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(message);
    }
}
