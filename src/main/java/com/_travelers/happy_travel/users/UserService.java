package com._travelers.happy_travel.users;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com._travelers.happy_travel.users.dto.UserMapper;
import com._travelers.happy_travel.users.dto.UserRegisterRequest;
import com._travelers.happy_travel.users.dto.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse addUser(UserRegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Role userRole = Role.USER;
        User user = UserMapper.toEntity(request, userRole);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return UserMapper.toDto(user);

    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return UserMapper.toDto(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username " + username + " not found"));
        return UserMapper.toDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public UserResponse updateUser(Long id, UserRegisterRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));

        existingUser.setUsername(request.username());
        existingUser.setEmail(request.email());
        existingUser.setPassword(passwordEncoder.encode(request.password())); // шифруем пароль

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDto(updatedUser);

    }
}


