package com._travelers.happy_travel.destinations;


import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.users.UserService;
import org.springframework.http.ResponseEntity;
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

}
