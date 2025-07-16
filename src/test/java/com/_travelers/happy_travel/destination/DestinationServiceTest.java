package com._travelers.happy_travel.destination;


import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.destinations.DestinationRepository;
import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.exceptions.EntityAlreadyExistsException;
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

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(destinationRepository);
    }

    @Test
    void getAllDestinations_whenDestinationsExist_returnsListOfDestinationsResponse() {
        List<DestinationResponseShort> expectedResult = List.of(
                new DestinationResponseShort());
        when(destinationRepository.findAll()).thenReturn(List.of(
                new DestinationResponseShort()));

        List<DestinationResponseShort> result = destinationService.getAllDestinations();
        assertEquals(expectedResult, result);
        verify(destinationRepository, times(1)).findAll();
    }

    @Test
    void getDestinationById_whenDestinationExists_returnsDestination() {
        Long id = 1L;
        Destination expectedResult = new Destination();
        when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(
                new Destination()));

        Destination result = destinationService.getDestinationById(id);
        assertEquals(expectedResult, result);
        verify(destinationRepository, times(1)).findById(id);
    }

    @Test
    void getDestinationById_whenDestinationDoesNotExist_returnsException() {
        Long id = 1L;
        Exception expectedException = new EntityNotFoundException(Destination.class.getSimpleName(), "id", id.toString());
        when(destinationRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> destinationService.getDestinationById(id));
        assertEquals(expectedException.getMessage(), resultException.getMessage());
        verify(destinationRepository, times(1)).findById(id);
    }

    @Test
    void getDestinationsByUserId_whenDestinationExists_returnsDestinationsList() {
        Long userId = 1L;
        List<DestinationResponse> expectedResult = List.of(
                new DestinationResponseShort());
        when(destinationRepository.findByUserId(eq(userId))).thenReturn(Optional.of(List.of(
                new Destination())));

        List<DestinationResponse> result = destinationService.getDestinationByUserId(userId);
        assertEquals(expectedResult, result);
        verify(destinationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getDestinationsByUserId_whenDestinationDoesNotExist_returnsException() {
        Long userId = 1L;
        String expectedMessage = "Destination with userId " + userId + " was not found";
        when(destinationRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> destinationService.getDestinationByUserId(userId));
        assertEquals(expectedMessage, exception.getMessage());
        verify(destinationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void addDestination_whenDestinationIsNewForUser_returnsDestinationResponse(){
        DestinationRequest destinationRequest = new DestinationRequest();
        DestinationResponse expectedResult = new DestinationResponse();
        when(destinationRepository.save(any(Destination.class))).thenReturn(
                new Destination());

        DestinationResponse result = destinationService.addDestination(destinationRequest);
        assertEquals(expectedResult, result);
        verify(destinationRepository, times(1)).save(destination);
    }

    @Test
    void addDestination_whenDestinationAlreadyExistsForUser_returnsException(){
        DestinationRequest destinationRequest = new DestinationRequest();
        String expectedMessage = "Destination with city City and country Country for user " + username + " already exists";
        when(destinationRepository.findByUserIdAndCityAndCountry(anyString())).thenReturn(Optional.of(
                new Destination()));

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> destinationService.addDestination(destinationRequest));
        assertEquals(expectedMessage, exception.getMessage());
        verify(destinationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void updateDestination_whenDestinationUpdateIsSuccessful_returnsDestinationResponse(){
        DestinationRequest destinationRequest = new DestinationRequest();
        DestinationResponse expectedResult = new DestinationResponse();
        when(destinationRepository.save(any(Destination.class))).thenReturn(
                new Destination());

        DestinationResponse result = destinationService.updateDestination(destinationRequest);
        assertEquals(expectedResult, result);
        verify(destinationRepository, times(1)).save(destination);
    }

    @Test
    void updateDestination_whenDestinationAlreadyExistsForUser_returnsException(){
        DestinationRequest destinationRequest = new DestinationRequest();
        Destination destination = new Destination();
        String username = "";
        String expectedMessage = "Destination with city City and country Country for user " + username + " already exists";
        when(destinationRepository.findByUserIdAndCityAndCountry(anyString())).thenReturn(Optional.of(destination));

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> destinationService.updateDestination(destinationRequest));
        assertEquals(expectedMessage, exception.getMessage());
        verify(destinationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void deleteDestination_whenDestinationExists_returnsMessage() {
        Long id  = 1L;
        Destination destination = new Destination();
        String expectedMessage = "Destination with id " + id + " deleted successfully";
        when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(destination));

        String result = destinationService.deleteDestination(id);
        assertEquals(expectedMessage, result);
        verify(destinationRepository, times(1)).findById(id);
        verify(destinationRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteDestination_whenDestinationDoesNotExist_returnsException() {
        Long id  = 1L;
        String expectedMessage = "Destination with id " + id + " was not found";
        when(destinationRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> destinationService.deleteDestination(id));
        assertEquals(expectedMessage, exception.getMessage());
        verify(destinationRepository, times(1)).findById(id);
    }
}