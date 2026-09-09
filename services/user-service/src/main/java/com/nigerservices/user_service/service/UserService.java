package com.nigerservices.user_service.service;
import java.util.List;
import java.util.stream.Collectors;
import com.nigerservices.user_service.dto.CreateUserRequest;
import com.nigerservices.user_service.dto.UserResponse;
import com.nigerservices.user_service.entity.User;
import com.nigerservices.user_service.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // On ne stocke JAMAIS le mot de passe en clair
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                savedUser.isEnabled(),
                savedUser.getCreatedAt()
        );
    }
public List<UserResponse> getAllUsers() {

    return userRepository.findAll()
            .stream()
            .map(user -> new UserResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getRole(),
                    user.isEnabled(),
                    user.getCreatedAt()
            ))
            .collect(Collectors.toList());
}
public UserResponse getUserById(Long id) {

    User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole(),
            user.isEnabled(),
            user.getCreatedAt()
    );
}
}
