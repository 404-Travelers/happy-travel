package com._travelers.happy_travel.destinations;

import com._travelers.happy_travel.destinations.dto.DestinationMapper;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {
    private final DestinationRepository destinationRepository;

    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<DestinationResponse> getAllDestinations(){
        List<Destination> destinations = destinationRepository.findAll();
        return destinations.stream().map(destination -> DestinationMapper.toDto(destination, destination.getUser())).toList();
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
}
