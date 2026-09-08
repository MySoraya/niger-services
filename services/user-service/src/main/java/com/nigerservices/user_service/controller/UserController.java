package com.nigerservices.user_service.controller;

import com.nigerservices.user_service.dto.CreateUserRequest;
import com.nigerservices.user_service.dto.UserResponse;
import com.nigerservices.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
@GetMapping("/{id}")
public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

    UserResponse response = userService.getUserById(id);

    return ResponseEntity.ok(response);
}
@GetMapping
public ResponseEntity<List<UserResponse>> getAllUsers() {

    List<UserResponse> users = userService.getAllUsers();

    return ResponseEntity.ok(users);
}
}
