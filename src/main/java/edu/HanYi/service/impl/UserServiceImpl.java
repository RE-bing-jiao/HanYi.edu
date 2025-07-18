package edu.HanYi.service.impl;

import edu.HanYi.dto.request.UserCreateRequest;
import edu.HanYi.dto.response.UserResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.debug("Checking username availability: {}", request.username());
        if (userRepository.existsByUsername(request.username())) {
            log.error(TO_CONSOLE, "Username already exists: {}", request.username());
            throw new ResourceAlreadyExistsException("Username '" + request.username() + "' already exists");
        }

        log.debug("Checking email availability: {}", request.email());
        if (userRepository.existsByEmail(request.email())) {
            log.error(TO_CONSOLE, "Email already exists: {}", request.email());
            throw new ResourceAlreadyExistsException("Email '" + request.email() + "' already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());
        user.setRole(request.role());

        User savedUser = userRepository.save(user);
        log.info(TO_CONSOLE, "Created user ID: {}, username: {}", savedUser.getId(), savedUser.getUsername());
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info(TO_CONSOLE, "Fetched {} users", users.size());
        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "User not found: ID={}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Integer id, UserCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "User not found: ID={}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        if (!user.getUsername().equals(request.username())) {
            log.debug("Checking new username availability: {}", request.username());
            if (userRepository.existsByUsername(request.username())) {
                log.error(TO_CONSOLE, "Username already exists: {}", request.username());
                throw new ResourceAlreadyExistsException("Username '" + request.username() + "' already exists");
            }
            user.setUsername(request.username());
        }

        if (!user.getEmail().equals(request.email())) {
            log.debug("Checking new email availability: {}", request.email());
            if (userRepository.existsByEmail(request.email())) {
                log.error(TO_CONSOLE, "Email already exists: {}", request.email());
                throw new ResourceAlreadyExistsException("Email '" + request.email() + "' already exists");
            }
            user.setEmail(request.email());
        }

        user.setPassword(request.password());
        user.setRole(request.role());

        User updatedUser = userRepository.save(user);
        log.info(TO_CONSOLE, "Updated user ID: {}", id);
        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        log.debug("Checking user existence ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.error(TO_CONSOLE, "User not found: ID={}", id);
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info(TO_CONSOLE, "Deleted user ID: {}", id);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFlashcards().stream()
                        .map(flashcard -> new UserResponse.FlashcardResponse(
                                flashcard.getId(),
                                flashcard.getFrontText(),
                                flashcard.getBackText()
                        ))
                        .toList(),
                user.getEntries().stream()
                        .map(entry -> new UserResponse.EntryResponse(
                                entry.getId(),
                                entry.getCourse().getId(),
                                entry.getCourse().getHeader()
                        ))
                        .toList()
        );
    }
}