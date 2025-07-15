package com._travelers.happy_travel.destination;


import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.destinations.DestinationRepository;
import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DestinationServiceTest {
    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationService destinationService;

    private DestinationResponse destinationResponse;
    private Destination destinationEntity;

    @BeforeEach
    void setUp() {
        destinationResponse = new DestinationResponse();
        destinationEntity = new Destination();
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    void getAllDestinations_whenDestinationsExist_returnsListOfDestinationsResponse() {
        List<DestinationResponse> expectedResult = List.of(destinationResponse);
        when(destinationRepository.findAll()).thenReturn(List.of(destinationEntity));
        List<DestinationResponse> result = destinationService.getAllDestinations();

        assertEquals(expectedResult, result);
        verify(destinationRepository, times(1)).findAll();
    }

    @Test
    void getDestinationById_whenDestinationExists_returnsDestination() {
        when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(destinationEntityRepo));
        Destination result = destinationService.getDestinationById(id);

        assertEquals(destinationEntity, result);
        verify(destinationRepository, times(1)).findById(id);
    }

    @Test
    void getDestinationById_whenDestinationDoesNotExist_returnsException() {
        Exception expectedException = new EntityNotFoundException(Destination.class.getSimpleName(), "id", id.toString());
        when(destinationRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> destinationService.getDestinationById(id));
        assertEquals(expectedException.getMessage(), resultException.getMessage());
        verify(destinationRepository, times(1)).findById(id);
    }
}