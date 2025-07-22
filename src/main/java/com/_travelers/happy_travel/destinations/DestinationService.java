package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.destinations.dto.DestinationMapper;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final UserRepository userRepository;

    public DestinationService(DestinationRepository destinationRepository, UserRepository userRepository) {
        this.destinationRepository = destinationRepository;
        this.userRepository = userRepository;
    }

    public List<DestinationResponse> getAllDestinations(){
        List<Destination> destinations = destinationRepository.findAll();
        return destinations.stream().map(destination -> DestinationMapper.toDto(destination))
                .collect(Collectors.toList());
    }

    // public List<DestinationResponse> getFilteredDestinations(){}

    public DestinationResponse getDestinationById(Long id){
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Destination", "id", id.toString()));
        return DestinationMapper.toDto(destination);
    }

    public List<DestinationResponse> getDestinationByUserId(Long userId){
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
}
