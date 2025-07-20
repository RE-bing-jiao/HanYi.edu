package edu.HanYi.service;

import edu.HanYi.dto.request.EntryCreateRequest;
import edu.HanYi.dto.response.EntryResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.CourseRepository;
import edu.HanYi.repository.EntryRepository;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.impl.EntryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EntryServiceImpl entryService;

    private EntryCreateRequest validRequest;
    private Entry existingEntry;
    private User existingUser;
    private Course existingCourse;
    private final Integer NON_EXISTING_ID = 10000;

    @BeforeEach
    void setUp() {
        validRequest = new EntryCreateRequest(
                1,
                1,
                LocalDateTime.now()
        );

        existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("user");

        existingCourse = new Course();
        existingCourse.setId(1);
        existingCourse.setHeader("Header");

        existingEntry = new Entry();
        existingEntry.setId(1);
        existingEntry.setUser(existingUser);
        existingEntry.setCourse(existingCourse);
        existingEntry.setEntryDate(LocalDateTime.now());
    }

    @Test
    void createEntry_ValidRequest_ShouldCreate() {
        when(entryRepository.existsByUserIdAndCourseId(validRequest.userId(), validRequest.courseId()))
                .thenReturn(false);
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.of(existingUser));
        when(courseRepository.findById(validRequest.courseId())).thenReturn(Optional.of(existingCourse));
        when(entryRepository.save(any(Entry.class))).thenReturn(existingEntry);

        EntryResponse response = entryService.createEntry(validRequest);

        assertNotNull(response);
        assertEquals(existingUser.getId(), response.userId());
        verify(entryRepository).existsByUserIdAndCourseId(validRequest.userId(), validRequest.courseId());
        verify(entryRepository).save(any(Entry.class));
    }

    @Test
    void createEntry_InvalidRequest_ThrowsResourceAlreadyExistsException() {
        when(entryRepository.existsByUserIdAndCourseId(validRequest.userId(), validRequest.courseId()))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> entryService.createEntry(validRequest)
        );
        verify(entryRepository).existsByUserIdAndCourseId(validRequest.userId(), validRequest.courseId());
        verify(entryRepository, never()).save(any());
    }

    @Test
    void createEntry_InvalidRequest_ThrowsResourceNotFoundExceptionForUser() {
        when(entryRepository.existsByUserIdAndCourseId(validRequest.userId(), validRequest.courseId()))
                .thenReturn(false);
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> entryService.createEntry(validRequest)
        );
        verify(userRepository).findById(validRequest.userId());
        verify(entryRepository, never()).save(any());
    }

    @Test
    void createEntry_InvalidRequest_ThrowsResourceNotFoundExceptionForCourse() {
        when(entryRepository.existsByUserIdAndCourseId(validRequest.userId(), validRequest.courseId()))
                .thenReturn(false);
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.of(existingUser));
        when(courseRepository.findById(validRequest.courseId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> entryService.createEntry(validRequest)
        );
        verify(courseRepository).findById(validRequest.courseId());
        verify(entryRepository, never()).save(any());
    }

    @Test
    void getEntryById_ValidRequest_ShouldFind() {
        when(entryRepository.findById(1)).thenReturn(Optional.of(existingEntry));

        EntryResponse response = entryService.getEntryById(1);

        assertEquals(existingUser.getId(), response.userId());
        verify(entryRepository).findById(1);
    }

    @Test
    void getEntryById_InvalidRequest_ThrowsResourceNotFoundException() {
        when(entryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> entryService.getEntryById(NON_EXISTING_ID)
        );
        verify(entryRepository).findById(NON_EXISTING_ID);
    }

    @Test
    void getEntriesByUserId_ValidRequest_ShouldFind() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(entryRepository.findByUserId(1)).thenReturn(List.of(existingEntry));

        List<EntryResponse> response = entryService.getEntriesByUserId(1);

        assertEquals(1, response.size());
        assertEquals(existingUser.getId(), response.get(0).userId());
        verify(userRepository).existsById(1);
        verify(entryRepository).findByUserId(1);
    }

    @Test
    void getEntriesByUserId_InvalidRequest_ThrowsResourceNotFoundException() {
        when(userRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> entryService.getEntriesByUserId(NON_EXISTING_ID)
        );
        verify(userRepository).existsById(NON_EXISTING_ID);
        verify(entryRepository, never()).findByUserId(any());
    }

    @Test
    void getEntriesByCourseId_ValidRequest_ShouldFind() {
        when(courseRepository.existsById(1)).thenReturn(true);
        when(entryRepository.findByCourseId(1)).thenReturn(List.of(existingEntry));

        List<EntryResponse> response = entryService.getEntriesByCourseId(1);

        assertEquals(1, response.size());
        assertEquals(existingCourse.getId(), response.get(0).courseId());
        verify(courseRepository).existsById(1);
        verify(entryRepository).findByCourseId(1);
    }

    @Test
    void getEntriesByCourseId_InvalidRequest_ThrowsResourceNotFoundException() {
        when(courseRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> entryService.getEntriesByCourseId(NON_EXISTING_ID)
        );
        verify(courseRepository).existsById(NON_EXISTING_ID);
        verify(entryRepository, never()).findByCourseId(any());
    }

    @Test
    void deleteEntry_ValidRequest_ShouldDelete() {
        when(entryRepository.existsById(1)).thenReturn(true);
        doNothing().when(entryRepository).deleteById(1);

        entryService.deleteEntry(1);
        verify(entryRepository).deleteById(1);
    }

    @Test
    void deleteEntry_InvalidRequest_ThrowsResourceNotFoundException() {
        when(entryRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> entryService.deleteEntry(NON_EXISTING_ID)
        );
        verify(entryRepository, never()).deleteById(any());
    }
}
