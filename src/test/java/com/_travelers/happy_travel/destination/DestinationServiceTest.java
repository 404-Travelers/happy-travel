package com._travelers.happy_travel.destination;


import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.destinations.DestinationRepository;
import com._travelers.happy_travel.destinations.DestinationService;
import com._travelers.happy_travel.destinations.dto.DestinationRequest;
import com._travelers.happy_travel.destinations.dto.DestinationResponseShort;
import com._travelers.happy_travel.exceptions.EntityAlreadyExistsException;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.destinations.dto.DestinationResponse;
import com._travelers.happy_travel.users.Role;
import com._travelers.happy_travel.users.User;
import com._travelers.happy_travel.users.UserService;
import com._travelers.happy_travel.users.dto.UserResponse;
import com._travelers.happy_travel.users.dto.UserResponseShort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DestinationServiceTest {
    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationService destinationService;

    @Mock
    private UserService userService;

    private Destination destination;
    private DestinationRequest destinationRequest;
    private DestinationResponse destinationResponse;
    private DestinationResponseShort destinationResponseShort;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.USER, new ArrayList<Destination>());
        destination = new Destination(1L, "Spain", "Valencia", "Nice", "image.jpg", user);
        destinationRequest = new DestinationRequest("Spain", "Valencia","Nice", "image.jpg");
        destinationResponse = new DestinationResponse("Spain", "Valencia","Nice", "image.jpg", new UserResponseShort("Kate"));
        destinationResponseShort = new DestinationResponseShort("Spain", "Valencia", "image.jpg", new UserResponseShort("Kate"));
    }

    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(destinationRepository);
    }

    @Nested
    @DisplayName("GET destinations")
    class GetDestinationsTests{

        @Test
        void getAllDestinations_whenDestinationsExist_returnsDestinationsList() {
            when(destinationRepository.findAll()).thenReturn(List.of(destination));

            List<DestinationResponseShort> result = destinationService.getAllDestinations();
            assertEquals(List.of(destinationResponseShort), result);
            verify(destinationRepository, times(1)).findAll();
        }

        @Test
        void getAllDestinations_whenDestinationsEmpty_returnsEmptyList() {
            when(destinationRepository.findAll()).thenReturn(List.of());

            List<DestinationResponseShort> result = destinationService.getAllDestinations();
            assertEquals(Collections.emptyList(), result);
            verify(destinationRepository, times(1)).findAll();
        }

        @Test
        void getFilteredDestinations_whenCityAndCountryPresent_returnsDestinationsList() {
            when(destinationRepository.findByCityContainingIgnoreCaseAndCountryContainingIgnoreCase(eq("rome"), eq("italy"))).thenReturn(List.of(destination));

            List<DestinationResponse> result = destinationService.getFilteredDestinations("rome", "italy");
            assertEquals(List.of(destinationResponse), result);
            verify(destinationRepository, times(1)).findByCityContainingIgnoreCaseAndCountryContainingIgnoreCase(eq("rome"), eq("italy"));
        }

        @Test
        void getFilteredDestinations_whenCityPresent_returnsDestinationsList() {
            when(destinationRepository.findByCityContainingIgnoreCase(eq("Rome"))).thenReturn(List.of(destination));

            List<DestinationResponse> result = destinationService.getFilteredDestinations("Rome", null);
            assertEquals(List.of(destinationResponse), result);
            verify(destinationRepository, times(1)).findByCityContainingIgnoreCase(eq("Rome"));
        }

        @Test
        void getFilteredDestinations_whenCountryPresent_returnsDestinationsList() {
            when(destinationRepository.findByCountryContainingIgnoreCase(eq("Italy"))).thenReturn(List.of(destination));

            List<DestinationResponse> result = destinationService.getFilteredDestinations(null, "Italy");
            assertEquals(List.of(destinationResponse), result);
            verify(destinationRepository, times(1)).findByCountryContainingIgnoreCase(eq("Italy"));
        }

        @Test
        void getFilteredDestinations_whenNoParameters_returnsEmptyList() {
            List<DestinationResponse> result = destinationService.getFilteredDestinations("", null);
            assertEquals(Collections.emptyList(), result);
        }

        @Test
        void getDestinationById_whenDestinationExists_returnsDestination() {
            Long id = 1L;
            when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(destination));

            DestinationResponse result = destinationService.getDestinationById(id);
            assertEquals(destinationResponse, result);
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
        void getDestinationsByUserUsername_whenDestinationsExists_returnsDestinationsList() {
            String username = user.getUsername();
            when(userService.getByUsername(eq(username))).thenReturn(user);
            when(destinationRepository.findByUser(eq(user))).thenReturn(List.of(destination));

            List<DestinationResponse> result = destinationService.getDestinationsByUserUsername(username);
            assertEquals(List.of(destinationResponse), result);
            verify(destinationRepository, times(1)).findByUser(any(User.class));
            verify(userService, times(1)).getByUsername(eq(username));
        }

        @Test
        void getDestinationsByUserUsername_whenDestinationsEmpty_returnsEmptyList() {
            String username = user.getUsername();
            when(userService.getByUsername(eq(username))).thenReturn(user);
            when(destinationRepository.findByUser(eq(user))).thenReturn(List.of());

            List<DestinationResponse> result = destinationService.getDestinationsByUserUsername(username);
            assertEquals(Collections.emptyList(), result);
            verify(destinationRepository, times(1)).findByUser(any(User.class));
            verify(userService, times(1)).getByUsername(eq(username));
        }

        @Test
        void getDestinationsByUserUsername_whenUserDoesNotExist_returnsException() {
            String username = user.getUsername();
            when(userService.getByUsername(eq(username))).thenThrow( new EntityNotFoundException(User.class.getSimpleName(), "username", username));
            String expectedMessage = "User with username " + username + " not found";

            Exception exception = assertThrows(EntityNotFoundException.class, () -> destinationService.getDestinationsByUserUsername(username));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userService, times(1)).getByUsername(eq(username));
        }
    }

    @Nested
    @DisplayName("ADD destinations")
    class AddDestinationsTests {

        @Test
        void addDestination_whenValidRequest_returnsDestinationResponse(){
            when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

            DestinationResponse result = destinationService.addDestination(destinationRequest, user);
            assertEquals(destinationResponse, result);
            verify(destinationRepository, times(1)).save(any(Destination.class));
        }
    }

    @Nested
    @DisplayName("UPDATE destinations")
    class UpdateDestinationsTests {

        @Test
        void updateDestination_whenDestinationUpdateIsSuccessful_returnsDestinationResponse(){
            Long id = 1L;
            when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(destination));
            when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

            DestinationResponse result = destinationService.updateDestination(id, destinationRequest, user);
            assertEquals(destinationResponse, result);
            verify(destinationRepository, times(1)).save(destination);
        }

        @Test
        void updateDestination_whenDestinationNotFound_returnsDestinationResponse(){
            Long id = 1L;
            when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(destination));
            when(destinationRepository.save(any(Destination.class))).thenReturn(destination);

            DestinationResponse result = destinationService.updateDestination(id, destinationRequest, user);
            assertEquals(destinationResponse, result);
            verify(destinationRepository, times(1)).save(destination);
        }
    }

    @Nested
    @DisplayName("DELETE destinations")
    class DeleteDestinationTests {

        @Test
        void deleteDestination_whenDestinationExists_returnsMessage() {
            Long id = 1L;
            String expectedMessage = "Destination with id " + id + " deleted successfully";
            when(destinationRepository.findById(eq(id))).thenReturn(Optional.of(destination));

            String result = destinationService.deleteDestination(id, user);
            assertEquals(expectedMessage, result);
            verify(destinationRepository, times(1)).findById(id);
            verify(destinationRepository, times(1)).delete(any(Destination.class));
        }

        @Test
        void deleteDestination_whenDestinationDoesNotExist_returnsException() {
            Long id = 1L;
            String expectedMessage = "Destination with id " + id + " not found";
            when(destinationRepository.findById(eq(id))).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () -> destinationService.deleteDestination(id, user));
            assertEquals(expectedMessage, exception.getMessage());
            verify(destinationRepository, times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("HELPERS assertUserRole")
    class AssertUserTests {

        @Test
        void assertUserIsOwner_whenUserIsOwner_returnsVoid(){
            assertDoesNotThrow(() -> destinationService.assertUserIsOwner(destination, user));
        }

        @Test
        void assertUserIsOwner_whenUserIsNotOwner_returnsException(){
            User notOwner = new User();
            user.setId(10L);
            AccessDeniedException ex = assertThrows(AccessDeniedException.class, () ->
                    destinationService.assertUserIsOwner(destination, notOwner));
            assertEquals("You are not authorized to modify or delete this destination.", ex.getMessage());
        }

        @Test
        void assertUserIsOwnerOrAdmin_whenUserIsOwner_returnsVoid(){
            assertDoesNotThrow(() -> destinationService.assertUserIsOwnerOrAdmin(destination, user));
        }

        @Test
        void assertUserIsOwnerOrAdmin_whenUserIsAdmin_returnsVoid(){
            User admin = new User();
            admin.setRole(Role.ADMIN);
            assertDoesNotThrow(() -> destinationService.assertUserIsOwnerOrAdmin(destination, admin));
        }

        @Test
        void assertUserIsOwnerOrAdmin_whenUserIsNotOwnerNorAdmin_returnsException(){
            User notOwner = new User();
            user.setId(10L);
            AccessDeniedException ex = assertThrows(AccessDeniedException.class, () ->
                    destinationService.assertUserIsOwner(destination, notOwner));
            assertEquals("You are not authorized to modify or delete this destination.", ex.getMessage());

        }
    }
}