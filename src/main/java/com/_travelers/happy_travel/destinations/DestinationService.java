package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.destinations.dto.DestinationMapper;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final UserRepository userRepository;

    public List<DestinationResponseShort> getAllDestinations(){
        List<Destination> destinations = destinationRepository.findAll();
        return destinations.stream().map(DestinationMapper::toDtoShort)
                .toList();
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

        return filtered.stream()
                .map(DestinationMapper::toDto)
                .toList();
    }

    public DestinationResponse getDestinationById(Long id){
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Destination", "id", id.toString()));
        return DestinationMapper.toDto(destination);
    }

    public List<DestinationResponse> getDestinationsByUserId(Long userId){
        List<Destination> destinations = destinationRepository.findAll()
                .stream()
                .filter(destination->destination.getUser() != null && destination.getUser().getId().equals(userId))
                .toList();
        if (destinations.isEmpty()){
            throw new EntityNotFoundException("Destination", "userId", userId.toString());
        }

        return destinations.stream()
                .map(DestinationMapper::toDto)
                .toList();
    }

    public DestinationResponse addDestination(DestinationRequest destinationRequest, User user) {
        Destination destination = DestinationMapper.toEntity(destinationRequest, user);
        Destination savedDestination = destinationRepository.save(destination);
        return DestinationMapper.toDto(savedDestination);
    }

    public DestinationResponse updateDestination(Long id, DestinationRequest request, User user) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destination", "id", id.toString()));

//        if (!destination.getUser().getId().equals(user.getId())) {
//            return null;
//        }

        destination.setCountry(request.country());
        destination.setCity(request.city());
        destination.setDescription(request.description());
        destination.setImageUrl(request.imageUrl());

        Destination updated = destinationRepository.save(destination);
        return DestinationMapper.toDto(updated);
    }

    public String deleteDestination(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destination", "id", id.toString()));

//        if (!destination.getUser().getId().equals(user.getId())) {
//            return null;
//        }

        destinationRepository.delete(destination);
        return "Destination deleted successfully";
    }
}
