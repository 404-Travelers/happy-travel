package com._travelers.happy_travel.destinations;


import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.security.CustomUserDetail;
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

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponse> getDestinationById(@PathVariable Long id) {
        DestinationResponse destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DestinationResponse>> getDestinationsByUserId(@AuthenticationPrincipal CustomUserDetail userDetail) {
//        System.out.println(userDetail.getId());
//        System.out.println("USER"+ userDetail.getUser());
        List<DestinationResponse> list = destinationService.getDestinationsByUserId(userDetail.getId());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DestinationResponse> addDestination(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody DestinationRequest request) {
        DestinationResponse created = destinationService.addDestination(request, userDetail.getUser());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/user/{destinationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DestinationResponse> updateDestination(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long destinationId,
            @RequestBody DestinationRequest request) {
        DestinationResponse updated = destinationService.updateDestination(destinationId, request, userDetail.getUser());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteDestination(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long id) {
        String message = destinationService.deleteDestination(id, userDetail.getUser());
        return ResponseEntity.ok(message);
    }
}
