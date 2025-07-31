package com._travelers.happy_travel.users;

import com._travelers.happy_travel.destinations.Destination;
import com._travelers.happy_travel.exceptions.EntityAlreadyExistsException;
import com._travelers.happy_travel.exceptions.EntityNotFoundException;
import com._travelers.happy_travel.security.CustomUserDetail;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRegisterRequest userRegisterRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user  = new User(1L, "Kate", "kate.dev@gmail.com", "encoded-password", Role.USER, new ArrayList<Destination>());;
        userResponse = new UserResponse(1L, "Kate", "kate.dev@gmail.com", "USER");
        userRegisterRequest = new UserRegisterRequest("Kate", "kate.dev@gmail.com", "mypass1234*");
    }
    
    @AfterEach
    void afterTest(){
        verifyNoMoreInteractions(userRepository);
    }

    @Nested
    @DisplayName("GET users")
    class GetUserTests {

        @Test
        void getAllUsers_whenUsersExist_returnsListOfUsersResponse() {
            List<UserResponse> expectedResult = List.of(userResponse);
            when(userRepository.findAll()).thenReturn(List.of(user));
            List<UserResponse> result = userService.getAllUsers();

            assertEquals(expectedResult, result);
            verify(userRepository, times(1)).findAll();
        }

        @Test
        void getAllUsers_whenNoUsers_returnsEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<UserResponse> result = userService.getAllUsers();

            assertTrue(result.isEmpty());
            verify(userRepository, times(1)).findAll();
        }

        @Test
        void getUserByIdAdmin_whenUserExists_returnsUserResponse() {
            Long id = 1L;
            UserResponse expectedResult = userResponse;
            when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
            UserResponse result = userService.getUserByIdAdmin(id);

            assertEquals(expectedResult, result);
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void getUserByIdAdmin_whenUserDoesNotExist_returnsException() {
            Long id = 1L;
            Exception expectedException = new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
            when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

            Exception resultException = assertThrows(EntityNotFoundException.class, () -> userService.getUserByIdAdmin(id));
            assertEquals(expectedException.getMessage(), resultException.getMessage());
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void getOwnUser_whenUserExists_returnsUserResponse() {
            Long id = 1L;
            UserResponse expectedResult = userResponse;
            when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
            UserResponse result = userService.getOwnUser(id);

            assertEquals(expectedResult, result);
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void getOwnUser_whenUserDoesNotExist_returnsException() {
            Long id = 1L;
            Exception expectedException = new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
            when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

            Exception resultException = assertThrows(EntityNotFoundException.class, () -> userService.getOwnUser(id));
            assertEquals(expectedException.getMessage(), resultException.getMessage());
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        void getUserByUsername_whenUserExists_returnsUserResponse() {
            String username ="Kate";
            when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));
            User result = userService.getByUsername(username);

            assertEquals(user, result);
            verify(userRepository, times(1)).findByUsername(username);
        }

        @Test
        void getUserByUsername_whenUserDoesNotExist_returnsException() {
            String username = "Mike";
            String expectedMessage = "User with username \"" + username + "\" not found";
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.getByUsername(username));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).findByUsername(username);
        }
    }

    @Nested
    @DisplayName("POST users")
    class AddUserTests {

        @Test
        void addUser_whenUserIsNew_returnsUserResponse() {
            when(passwordEncoder.encode(any())).thenReturn("encoded-password");
            UserResponse expectedResult = userResponse;
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userRepository.existsByUsername(eq(userRegisterRequest.username()))).thenReturn(false);
            when(userRepository.existsByEmail(eq(userRegisterRequest.email()))).thenReturn(false);

            UserResponse result = userService.addUser(userRegisterRequest);

            assertEquals(expectedResult, result);
            verify(userRepository, times(1)).existsByUsername(userRegisterRequest.username());
            verify(userRepository, times(1)).existsByEmail(userRegisterRequest.email());
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        void addUser_whenUsernameAlreadyExists_returnsException() {
            UserRegisterRequest userRequest = userRegisterRequest;
            String expectedMessage = "User with username \"" + userRequest.username() + "\" already exists";
            when(userRepository.existsByUsername(eq(userRegisterRequest.username()))).thenReturn(true);

            Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.addUser(userRequest));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).existsByUsername(userRegisterRequest.username());
        }

        @Test
        void addUser_whenEmailAlreadyExists_returnsException() {
            UserRegisterRequest userRequest = userRegisterRequest;
            String expectedMessage = "User with email \"" + userRequest.email() + "\" already exists";
            when(userRepository.existsByEmail(eq(userRegisterRequest.email()))).thenReturn(true);
            when(userRepository.existsByUsername(eq(userRegisterRequest.username()))).thenReturn(false);

            Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.addUser(userRequest));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).existsByEmail(userRegisterRequest.email());
            verify(userRepository, times(1)).existsByUsername(userRegisterRequest.username());
        }
    }

    @Nested
    @DisplayName("PUT users")
    class UpdateUserTests {

        @Test
        void updateUser_whenUserRequestIsValid_returnsUserResponse() {
            Long id = 1L;
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            UserResponse result = userService.updateOwnUser(1L, userRegisterRequest);

            assertEquals(userResponse, result);
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).save(user);
        }

        @Test
        void updateUser_whenIdDoesNotExist_returnsException() {
            Long id = 10L;
            String expectedMessage = "User with id \"" + id + "\" not found";
            when(userRepository.findById(eq(id))).thenReturn(Optional.empty());
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

            Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.updateOwnUser(id, userRegisterRequest));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).findById(id);
//        verify(userRepository, times(1)).findByUsername(userRegisterRequest.username());
        }

        @Test
        void updateUser_whenUsernameAlreadyExists_returnsException() {
            Long id = 10L;
            userRegisterRequest = new UserRegisterRequest("Mark", "mark@gamil.com", "1234");
            String expectedMessage = "User with username \"" + userRegisterRequest.username() + "\" already exists";
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));


            Exception exception = assertThrows(EntityAlreadyExistsException.class, () -> userService.updateOwnUser(id, userRegisterRequest));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).findByUsername(userRegisterRequest.username());
        }

        @Test
        void updateUser_whenUsernameChangedAndNotExists_returnsUpdatedUser() {
            Long id = 1L;
            userRegisterRequest = new UserRegisterRequest("Olivia", "kate.dev@gmail.com", "newpass456*");
            userResponse = new UserResponse(1L, "Olivia", "kate.dev@gmail.com", "USER");
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername("Olivia")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(any())).thenReturn("encoded-password");
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserResponse result = userService.updateOwnUser(id, userRegisterRequest);

            assertEquals(userResponse, result);
            verify(userRepository, times(1)).findById(id);
            verify(userRepository, times(1)).findByUsername("Olivia");
            verify(userRepository, times(1)).save(user);
        }
    }

    @Nested
    @DisplayName("DELETE users")
    class DeleteUserTests {

        @Test
        void deleteUserAdmin_whenUserExists_returnsMessage() {
            Long id = 1L;
            User user = new User();
            String expectedMessage = "User with id " + id + " deleted successfully";
            when(userRepository.existsById(eq(id))).thenReturn(true);
            doNothing().when(userRepository).deleteById(eq(id));
            String result = userService.deleteUserByIdAdmin(id);

            assertEquals(expectedMessage, result);
            verify(userRepository, times(1)).existsById(id);
            verify(userRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteUserAdmin_whenUserDoesNotExist_returnsException() {
            Long id = 1L;
            String expectedMessage = "User with id \"" + id + "\" not found";
            when(userRepository.existsById(eq(id))).thenReturn(false);

            Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUserByIdAdmin(id));

            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).existsById(id);
        }

        @Test
        void deleteOwnUser_whenUserExists_returnsMessage() {
            Long id = 1L;
            User user = new User();
            String expectedMessage = "User with id " + id + " deleted successfully";
            when(userRepository.existsById(eq(id))).thenReturn(true);
            doNothing().when(userRepository).deleteById(eq(id));
            String result = userService.deleteOwnUser(id);

            assertEquals(expectedMessage, result);
            verify(userRepository, times(1)).existsById(id);
            verify(userRepository, times(1)).deleteById(id);
        }

        @Test
        void deleteOwnUser_whenUserDoesNotExist_returnsException() {
            Long id = 1L;
            String expectedMessage = "User with id \"" + id + "\" not found";
            when(userRepository.existsById(eq(id))).thenReturn(false);

            Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteOwnUser(id));

            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).existsById(id);
        }
    }

    @Nested
    @DisplayName("LOAD users")
    class LoadUserTests {

        @Test
        void loadUserByUsername_whenUserExists_returnsUserDetail() {
            String username = "Kate";
            UserDetails expectedResult = new CustomUserDetail(user);
            when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));
            UserDetails result = userService.loadUserByUsername(username);

            assertEquals(expectedResult.getUsername(), result.getUsername());
            assertEquals(expectedResult.getAuthorities(), result.getAuthorities());
            assertEquals(expectedResult.getPassword(), result.getPassword());
            assertEquals(expectedResult.getClass(), result.getClass());
            verify(userRepository, times(1)).findByUsername(username);
        }

        @Test
        void loadUserByUsername_whenUserDoesNotExist_returnsException() {
            String username = "Mike";
            String expectedMessage = "User with username \"" + username + "\" not found";
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

            Exception exception = assertThrows(EntityNotFoundException.class, () -> userService.loadUserByUsername(username));
            assertEquals(expectedMessage, exception.getMessage());
            verify(userRepository, times(1)).findByUsername(username);
        }
    }
}
