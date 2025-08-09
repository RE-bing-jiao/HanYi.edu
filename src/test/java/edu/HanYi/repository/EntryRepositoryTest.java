package edu.HanYi.repository;

import edu.HanYi.model.Category;
import edu.HanYi.model.Course;
import edu.HanYi.model.Entry;
import edu.HanYi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EntryRepositoryTest {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole(User.Role.STUDENT);
        entityManager.persist(testUser);

        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Test Description");
        entityManager.persist(category);

        testCourse = new Course();
        testCourse.setHeader("Test Course");
        testCourse.setDescription("Test Description");
        testCourse.setCategory(category);
        testCourse.setEntryDate(LocalDateTime.now().plusDays(1));
        testCourse.setExitDate(LocalDateTime.now().plusDays(30));
        entityManager.persist(testCourse);
    }

    @Test
    void findByUserIdShouldReturnEntriesWhenUserExists() {
        Entry entry = createValidEntry(testUser, testCourse);
        entryRepository.save(entry);

        List<Entry> foundEntries = entryRepository.findByUserId(testUser.getId());

        assertFalse(foundEntries.isEmpty());
        assertEquals(1, foundEntries.size());
        assertEquals(testUser.getId(), foundEntries.get(0).getUser().getId());
    }

    @Test
    void findByCourseIdShouldReturnEntriesWhenCourseExists() {
        Entry entry = createValidEntry(testUser, testCourse);
        entryRepository.save(entry);

        List<Entry> foundEntries = entryRepository.findByCourseId(testCourse.getId());

        assertFalse(foundEntries.isEmpty());
        assertEquals(1, foundEntries.size());
        assertEquals(testCourse.getId(), foundEntries.get(0).getCourse().getId());
    }

    @Test
    void existsByUserIdAndCourseIdShouldReturnTrueWhenEntryExists() {
        Entry entry = createValidEntry(testUser, testCourse);
        entryRepository.save(entry);

        assertTrue(entryRepository.existsByUserIdAndCourseId(testUser.getId(), testCourse.getId()));
    }

    @Test
    void existsByUserIdAndCourseIdShouldReturnFalseWhenEntryDoesNotExist() {
        assertFalse(entryRepository.existsByUserIdAndCourseId(-1, -1));
    }

    private Entry createValidEntry(User user, Course course) {
        Entry entry = new Entry();
        entry.setUser(user);
        entry.setCourse(course);
        entry.setEntryDate(LocalDateTime.now().plusDays(1));
        return entry;
    }
}
