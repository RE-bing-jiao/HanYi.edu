package edu.HanYi.controller;

import edu.HanYi.constants.LoggingConstants;
import edu.HanYi.dto.request.UserCreateRequest;
import edu.HanYi.dto.response.UserResponse;
import edu.HanYi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.USER_CREATE, request.username());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.debug(LoggingConstants.USER_FETCH);
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        log.debug(LoggingConstants.USER_FETCH, id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.USER_UPDATE, id);
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info(TO_CONSOLE, LoggingConstants.USER_DELETE, id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}