package edu.HanYi.service.impl;

import edu.HanYi.dto.request.EntryCreateRequest;
import edu.HanYi.dto.response.EntryResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.CourseRepository;
import edu.HanYi.repository.EntryRepository;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.EntryService;
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
public class EntryServiceImpl implements EntryService {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EntryResponse createEntry(EntryCreateRequest request) {
        log.debug("Checking if entry exists for user ID: {} and course ID: {}",
                request.userId(), request.courseId());
        if (entryRepository.existsByUserIdAndCourseId(request.userId(), request.courseId())) {
            log.error(TO_CONSOLE, "Entry already exists for user ID: {} and course ID: {}",
                    request.userId(), request.courseId());
            throw new ResourceAlreadyExistsException("Entry already exists for this user and course");
        }

        log.debug("Fetching user with ID: {}", request.userId());
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "User not found with ID: {}", request.userId());
                    return new ResourceNotFoundException("User not found with id: " + request.userId());
                });

        log.debug("Fetching course with ID: {}", request.courseId());
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Course not found with ID: {}", request.courseId());
                    return new ResourceNotFoundException("Course not found with id: " + request.courseId());
                });

        log.debug("Creating new entry for user: {} and course: {}",
                user.getUsername(), course.getHeader());
        Entry entry = Entry.builder()
                .user(user)
                .course(course)
                .entryDate(request.entryDate())
                .build();

        Entry savedEntry = entryRepository.save(entry);
        log.info(TO_CONSOLE, "Created entry ID: {} for user ID: {} and course ID: {}",
                savedEntry.getId(), request.userId(), request.courseId());
        return mapToResponse(savedEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getAllEntries() {
        List<Entry> entries = entryRepository.findAll();
        return entries.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EntryResponse getEntryById(Integer id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Entry not found with ID: {}", id);
                    return new ResourceNotFoundException("Entry not found with id: " + id);
                });
        log.debug("Found entry ID: {} for user ID: {}", id, entry.getUser().getId());
        return mapToResponse(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getEntriesByUserId(Integer userId) {
        log.debug("Fetching entries for user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            log.error(TO_CONSOLE, "User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Entry> entries = entryRepository.findByUserId(userId);
        return entries.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getEntriesByCourseId(Integer courseId) {
        log.debug("Fetching entries for course ID: {}", courseId);

        if (!courseRepository.existsById(courseId)) {
            log.error(TO_CONSOLE, "Course not found with ID: {}", courseId);
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        List<Entry> entries = entryRepository.findByCourseId(courseId);
        return entries.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteEntry(Integer id) {
        log.debug("Checking if entry exists with ID: {}", id);
        if (!entryRepository.existsById(id)) {
            log.error(TO_CONSOLE, "Delete failed - entry not found with ID: {}", id);
            throw new ResourceNotFoundException("Entry not found with id: " + id);
        }
        entryRepository.deleteById(id);
        log.info(TO_CONSOLE, "Deleted entry ID: {}", id);
    }

    private EntryResponse mapToResponse(Entry entry) {
        return new EntryResponse(
                entry.getId(),
                entry.getUser().getId(),
                entry.getUser().getUsername(),
                entry.getCourse().getId(),
                entry.getCourse().getHeader(),
                entry.getEntryDate()
        );
    }
}