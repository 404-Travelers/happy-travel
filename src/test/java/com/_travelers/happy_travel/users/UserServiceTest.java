package com._travelers.happy_travel.users;

import com._travelers.happy_travel.exceptions.EntityAlreadyExistsException;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.users.dto.UserRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
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
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {

    }
    
    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(userRepository);
    }
    
    @Test
    void getAllUsers_whenUsersExist_returnsListOfUsersResponse() {
        List<UserResponse> expectedResult = List.of(userResponse);
        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        List<UserResponse> result = userService.getAllUsers();

        assertEquals(expectedResult, result);
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    void getUserById_whenUserExists_returnsUser() {
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(userEntityRepo));
        User result = userService.getUserById(id);

        assertEquals(userEntity, result);
        verify(userRepository, times(1)).findById(id);
    }
    
    @Test
    void getUserById_whenUserDoesNotExist_returnsException() {
        Exception expectedException = new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));
        assertEquals(expectedException.getMessage(), resultException.getMessage());
        verify(userRepository, times(1)).findById(id);
    }
    
    @Test
    void getUserByUsername_whenUserExists_returnsUser() {
        String username ="User 1";
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(userEntity));
        User result = userService.getUserByUsername(username);

        assertEquals(userEntityRepo, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByUsername_whenUserDoesNotExist_returnsException() {
        String username = "user 2";
        String expectedMessage = "User with username " + username + " was not found";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername(username));
        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void addUser_whenUserIsNew_returnsUserResponse(){
        UserRequest userRequest = new UserRequest();
        UserResponse userResponse = new UserResponse();
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.addUser(userRequest);

        assertEquals(userResponse, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addUser_whenUsernameAlreadyExists_returnsException(){
        UserRequest userRequest = new UserRequest();
        User user = new User();
        String expectedMessage = "User with username " + username + " already exists";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.addUser(userRequest));
        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void updateUser_whenUserRequestIsValid_returnsUserResponse(){
        UserRequest userRequest = new UserRequest();
        UserResponse userResponse = new UserResponse();
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.updateUser(userRequest);

        assertEquals(userResponse, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_whenUsernameAlreadyExists_returnsException(){
        UserRequest userRequest = new UserRequest();
        User user = new User();
        String expectedMessage = "User with username " + username + " already exists";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.updateUser(userRequest));
        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void deleteUser_whenUserExists_returnsMessage() {
        Long id  = 1L;
        User user = new User();
        String expectedMessage = "User with id " + id + " deleted successfully";
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
        String result = userService.deleteUser(id);

        assertEquals(expectedMessage, result);
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(user);
    }

        @Test
    void deleteUser_whenUserDoesNotExist_returnsException() {
        Long id  = 1L;

        String expectedMessage = "User with id " + id + " was not found";
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(id));

        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findById(id);
    }
}
