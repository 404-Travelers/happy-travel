package com._travelers.happy_travel.users;

import com._travelers.happy_travel.exceptions.EntityNotFoundException;
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
}
