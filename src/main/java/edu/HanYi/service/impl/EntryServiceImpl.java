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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public EntryResponse createEntry(EntryCreateRequest request) {
        if (entryRepository.existsByUserIdAndCourseId(request.userId(), request.courseId())) {
            throw new ResourceAlreadyExistsException("Entry already exists for this user and course");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        Entry entry = Entry.builder()
                .user(user)
                .course(course)
                .entryDate(request.entryDate())
                .build();

        Entry savedEntry = entryRepository.save(entry);
        return mapToResponse(savedEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getAllEntries() {
        return entryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EntryResponse getEntryById(Integer id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entry not found with id: " + id));
        return mapToResponse(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getEntriesByUserId(Integer userId) {
        return entryRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntryResponse> getEntriesByCourseId(Integer courseId) {
        return entryRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteEntry(Integer id) {
        if (!entryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Entry not found with id: " + id);
        }
        entryRepository.deleteById(id);
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