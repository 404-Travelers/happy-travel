package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.security.CustomUserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping
    public ResponseEntity<List<DestinationResponseShort>> getAllDestinations() {
        List<DestinationResponseShort> list = destinationService.getAllDestinations();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<DestinationResponse>> getFilteredDestinations(@RequestParam (required = false) String city, @RequestParam (required = false) String country)  {
        List<DestinationResponse> list = destinationService.getFilteredDestinations(city, country);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<DestinationResponse> getDestinationById(@PathVariable Long destinationId) {
        DestinationResponse destination = destinationService.getDestinationById(destinationId);
        return ResponseEntity.ok(destination);
    }

    @GetMapping("/user")
    public ResponseEntity<List<DestinationResponse>> getDestinationsByUserUsername(@RequestParam String username) {
        List<DestinationResponse> list = destinationService.getDestinationsByUserUsername(username);
        return ResponseEntity.ok(list);
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DestinationResponse> addDestination(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid DestinationRequest request) {
        DestinationResponse created = destinationService.addDestination(request, userDetail.getUser());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{destinationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DestinationResponse> updateDestination(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long destinationId,
            @RequestBody @Valid DestinationRequest request) {
        DestinationResponse updated = destinationService.updateDestination(destinationId, request, userDetail.getUser());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{destinationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteDestination(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long destinationId) {
        String message = destinationService.deleteDestination(destinationId, userDetail.getUser());
        return ResponseEntity.ok(message);
    }
}
