package edu.HanYi.service;

import edu.HanYi.dto.request.UserCreateRequest;
import edu.HanYi.dto.response.UserResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static edu.HanYi.model.User.Role.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreateRequest validRequest;
    private User existingUser;
    private final Integer NON_EXISTING_ID = 10000;

    @BeforeEach
    void setUp() {
        validRequest = new UserCreateRequest(
                "user",
                "password",
                "test@mail.ru",
                STUDENT
        );

        existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("user");
        existingUser.setPassword("password");
        existingUser.setEmail("test@mail.ru");
        existingUser.setRole(STUDENT);
    }

    @Test
    void createUser_ValidRequest_ShouldCreate() {
        when(userRepository.existsByUsername(validRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(validRequest.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponse response = userService.createUser(validRequest);

        assertNotNull(response);
        assertEquals(validRequest.email(), response.username());
        verify(userRepository).existsByUsername(validRequest.username());
        verify(userRepository).existsByEmail(validRequest.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_InvalidRequest_ThrowsResourceAlreadyExistsExceptionForUsername() {
        when(userRepository.existsByUsername(validRequest.username())).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.createUser(validRequest)
        );
        verify(userRepository).existsByUsername(validRequest.username());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_InvalidRequest_ThrowsResourceAlreadyExistsExceptionForEmail() {
        when(userRepository.existsByUsername(validRequest.username())).thenReturn(false);
        when(userRepository.existsByEmail(validRequest.email())).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.createUser(validRequest)
        );
        verify(userRepository).existsByUsername(validRequest.username());
        verify(userRepository).existsByEmail(validRequest.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_ValidRequest_ShouldFind() {
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));

        UserResponse response = userService.getUserById(1);

        assertEquals(existingUser.getId(), response.id());
        verify(userRepository).findById(1);
    }

    @Test
    void getUserById_InvalidRequest_ThrowsResourceNotFoundException() {
        when(userRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(NON_EXISTING_ID)
        );
        verify(userRepository).findById(NON_EXISTING_ID);
    }

    @Test
    void updateUser_ValidRequest_ShouldUpdate() {
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("new_username")).thenReturn(false);
        when(userRepository.existsByEmail("new@mail.ru")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserCreateRequest updateRequest = new UserCreateRequest(
                "new_username",
                "new_password",
                "new@mail.ru",
                TEACHER
        );
        UserResponse response = userService.updateUser(1, updateRequest);

        assertEquals(updateRequest.email(), response.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_InvalidRequest_ThrowsResourceNotFoundException() {
        when(userRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(NON_EXISTING_ID, validRequest)
        );
        verify(userRepository).findById(NON_EXISTING_ID);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_InvalidRequest_ThrowsResourceAlreadyExistsExceptionForUsername() {
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        UserCreateRequest conflictRequest = new UserCreateRequest(
                "existinguser",
                "password",
                "test@mail.ru",
                STUDENT
        );

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.updateUser(1, conflictRequest)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_InvalidRequest_ThrowsResourceAlreadyExistsExceptionForEmail() {
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("existing@mail.ru")).thenReturn(true);

        UserCreateRequest conflictRequest = new UserCreateRequest(
                "user",
                "password",
                "existing@mail.ru",
                STUDENT
        );

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.updateUser(1, conflictRequest)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ValidRequest_ShouldDelete() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_InvalidRequest_ThrowsResourceNotFoundException() {
        when(userRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(NON_EXISTING_ID)
        );
        verify(userRepository, never()).deleteById(any());
    }
}