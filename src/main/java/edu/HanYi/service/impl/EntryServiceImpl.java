package edu.HanYi.service.impl;

import edu.HanYi.constants.LoggingConstants;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EntryResponse createEntry(EntryCreateRequest request) {
        log.debug(LoggingConstants.DEBUG_CHECK_ENTRY_EXISTS, request.userId(), request.courseId());
        if (entryRepository.existsByUserIdAndCourseId(request.userId(), request.courseId())) {
            log.error(TO_CONSOLE, LoggingConstants.ENTRY_EXISTS, request.userId(), request.courseId());
            throw new ResourceAlreadyExistsException("Entry already exists for this user and course");
        }

        log.debug(LoggingConstants.DEBUG_FETCH_USER, request.userId());
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, LoggingConstants.USER_NOT_FOUND_ID, request.userId());
                    return new ResourceNotFoundException("User not found with id: " + request.userId());
                });

        log.debug(LoggingConstants.DEBUG_FETCH_COURSE_ENTRY, request.courseId());
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, LoggingConstants.COURSE_NOT_FOUND_ID, request.courseId());
                    return new ResourceNotFoundException("Course not found with id: " + request.courseId());
                });

        log.debug(LoggingConstants.DEBUG_CREATE_ENTRY, user.getUsername(), course.getHeader());
        Entry entry = new Entry();
        entry.setCourse(course);
        entry.setUser(user);
        entry.setEntryDate(request.entryDate());

        Entry savedEntry = entryRepository.save(entry);
        log.info(TO_CONSOLE, LoggingConstants.ENTRY_CREATED, savedEntry.getId(), request.userId(), request.courseId());
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
                    log.error(TO_CONSOLE, LoggingConstants.ENTRY_NOT_FOUND_ID, id);
                    return new ResourceNotFoundException("Entry not found with id: " + id);
                });
        log.debug(LoggingConstants.DEBUG_FETCH_ENTRY, id, entry.getUser().getId());
        return mapToResponse(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getEntriesByUserId(Integer userId) {
        log.debug(LoggingConstants.DEBUG_FETCH_ENTRIES_USER, userId);

        if (!userRepository.existsById(userId)) {
            log.error(TO_CONSOLE, LoggingConstants.USER_NOT_FOUND_ID, userId);
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
        log.debug(LoggingConstants.DEBUG_FETCH_ENTRIES_COURSE, courseId);

        if (!courseRepository.existsById(courseId)) {
            log.error(TO_CONSOLE, LoggingConstants.COURSE_NOT_FOUND_ID, courseId);
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
        log.debug(LoggingConstants.DEBUG_CHECK_ENTRY_EXISTENCE, id);
        if (!entryRepository.existsById(id)) {
            log.error(TO_CONSOLE, LoggingConstants.ENTRY_DELETE_FAILED, id);
            throw new ResourceNotFoundException("Entry not found with id: " + id);
        }
        entryRepository.deleteById(id);
        log.info(TO_CONSOLE, LoggingConstants.ENTRY_DELETED, id);
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