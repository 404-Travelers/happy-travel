package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.destinations.dto.DestinationMapper;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.users.Role;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final UserService userService;

    public List<DestinationResponseShort> getAllDestinations(){
        List<Destination> destinations = destinationRepository.findAll();
        return listToDtoShort(destinations);
    }

    public List<DestinationResponse> getFilteredDestinations(String city, String country) {
        List<Destination> filtered;

        boolean cityIsEmpty = city == null || city.isBlank();
        boolean countryIsEmpty = country == null || country.isBlank();

        if (!cityIsEmpty && !countryIsEmpty) {
            filtered = destinationRepository.findByCityContainingIgnoreCaseAndCountryContainingIgnoreCase(city, country);
        } else if (!cityIsEmpty) {
            filtered = destinationRepository.findByCityContainingIgnoreCase(city);
        } else if (!countryIsEmpty) {
            filtered = destinationRepository.findByCountryContainingIgnoreCase(country);
        } else {
            filtered = destinationRepository.findAll();
        }

        return listToDto(filtered);
    }

    public DestinationResponse getDestinationById(Long id){
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Destination", "id", id.toString()));
        return DestinationMapper.toDto(destination);
    }

    public List<DestinationResponse> getDestinationsByUserUsername(String username){
        User user = userService.getByUsername(username);
        List<Destination> list = destinationRepository.findByUser(user);
        return list.stream()
                .map(DestinationMapper::toDto)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public DestinationResponse addDestination(DestinationRequest destinationRequest, User user) {
        Destination destination = DestinationMapper.toEntity(destinationRequest, user);
        Destination savedDestination = destinationRepository.save(destination);
        return DestinationMapper.toDto(savedDestination);
    }

    @PreAuthorize("isAuthenticated()")
    public DestinationResponse updateDestination(Long id, DestinationRequest request, User user) {
        Destination destination = findDestinationOrThrow(id);
        assertUserIsOwner(destination, user);

        destination.setCountry(request.country());
        destination.setCity(request.city());
        destination.setDescription(request.description());
        destination.setImageUrl(request.imageUrl());

        Destination updated = destinationRepository.save(destination);
        return DestinationMapper.toDto(updated);
    }

    @PreAuthorize("isAuthenticated()")
    public String deleteDestination(Long id, User user) {
        Destination destination = findDestinationOrThrow(id);
        assertUserIsOwnerOrAdmin(destination, user);
        destinationRepository.delete(destination);
        return "Destination with id " + id + " deleted successfully";
    }

    private Destination findDestinationOrThrow(Long id) {
    return destinationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Destination", "id", id.toString()));
    }


    private void assertUserIsOwner(Destination destination, User user) {
        if (!destination.getUser().getId().equals(user.getId()) ) {
            throw new AccessDeniedException("You are not authorized to modify or delete this destination.");
        }
    }

    private void assertUserIsOwnerOrAdmin(Destination destination, User user) {
        if (!destination.getUser().getId().equals(user.getId()) && !user.hasRole(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("You are not authorized to modify or delete this destination.");
        }
    }

    private List<DestinationResponse> listToDto (List<Destination> destinations){
        return destinations.stream()
                .map(DestinationMapper::toDto)
                .toList();
    }

    private List<DestinationResponseShort> listToDtoShort (List<Destination> destinations){
        return destinations.stream()
                .map(DestinationMapper::toDtoShort)
                .toList();
    }
}
