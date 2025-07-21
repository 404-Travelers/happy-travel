package com._travelers.happy_travel.users;

import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.exceptions.EntityAlreadyExistsException;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    private User user;
    private UserRegisterRequest userRegisterRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user  = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.ROLE_USER, new ArrayList<Destination>());;
        userResponse = new UserResponse("Kate", "kate.dev@gmail.com", "ROLE_USER");
        userRegisterRequest = new UserRegisterRequest("Kate", "mar@gmail.com", "mypass1234*");
    }
    
    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(userRepository);
    }
    
//    @Test
//    void getAllUsers_whenUsersExist_returnsListOfUsersResponse() {
//        List<UserResponse> expectedResult = List.of(userResponse);
//        when(userRepository.findAll()).thenReturn(List.of(user));
//        List<UserResponse> result = userService.getAllUsers();
//
//        assertEquals(expectedResult, result);
//        verify(userRepository, times(1)).findAll();
//    }
//
    @Test
    void getUserById_whenUserExists_returnsUser() {
        Long id = 1L;
        UserResponse expectedResult = userResponse;
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
        UserResponse result = userService.getUserByIdResponse(id);

        assertEquals(expectedResult, result);
        verify(userRepository, times(1)).findById(id);
    }
    
    @Test
    void getUserById_whenUserDoesNotExist_returnsException() {
        Long id = 1L;
        Exception expectedException = new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception resultException = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));
        assertEquals(expectedException.getMessage(), resultException.getMessage());
        verify(userRepository, times(1)).findById(id);
    }
    
    @Test
    void getUserByUsername_whenUserExists_returnsUser() {
        String username ="Kate";
        User expectedResult = user;
        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));
        User result = userService.getUserByUsername(username);

        assertEquals(expectedResult, result);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByUsername_whenUserDoesNotExist_returnsException() {
        String username = "Mike";
        String expectedMessage = "User with username " + username + " not found";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername(username));
        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void addUser_whenUserIsNew_returnsUserResponse(){
        UserResponse expectedResult = userResponse;
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.addUser(userRegisterRequest);

        assertEquals(expectedResult, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addUser_whenUsernameAlreadyExists_returnsException(){
        UserRegisterRequest userRequest = userRegisterRequest;
        String expectedMessage = "User with username " + userRequest.username() + " already exists";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.addUser(userRequest));
        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(userRequest.username());
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
        String expectedMessage = "User with username " + userRegisterRequest.username() + " already exists";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.updateUser(userRegisterRequest));
        assertEquals(expectedMessage, exception.getMessage());
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
    }

        @Test
    void deleteUser_whenUserDoesNotExist_returnsException() {
        Long id  = 1L;
        String expectedMessage = "User with id " + id + " not found";
        when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(id));

        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository, times(1)).findById(id);
    }
}
