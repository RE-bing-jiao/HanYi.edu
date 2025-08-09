package edu.HanYi.repository;

import edu.HanYi.model.Category;
import edu.HanYi.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setName("Name");
        testCategory.setDescription("Description");
        entityManager.persist(testCategory);
    }

    @Test
    void findByCategoryIdShouldReturnCoursesWhenCategoryExists() {
        Course course = createValidCourse(testCategory);
        courseRepository.save(course);
        List<Course> foundCourses = courseRepository.findByCategoryId(testCategory.getId());

        assertFalse(foundCourses.isEmpty());
        assertEquals(1, foundCourses.size());
        assertEquals("Header", foundCourses.get(0).getHeader());
    }

    @Test
    void findByCategoryIdShouldReturnEmptyListWhenCategoryDoesNotExist() {
        List<Course> foundCourses = courseRepository.findByCategoryId(-1);

        assertTrue(foundCourses.isEmpty());
    }

    @Test
    void existsByHeaderShouldReturnTrueWhenHeaderExists() {
        Course course = createValidCourse(testCategory);
        courseRepository.save(course);

        boolean exists = courseRepository.existsByHeader("Header");

        assertTrue(exists);
    }

    @Test
    void existsByHeaderShouldReturnFalseWhenHeaderDoesNotExist() {
        assertFalse(courseRepository.existsByHeader("Non-existent"));
    }

    private Course createValidCourse(Category category) {
        Course course = new Course();
        course.setHeader("Header");
        course.setDescription("Description");
        course.setCategory(category);
        course.setEntryDate(LocalDateTime.now().plusDays(1));
        course.setExitDate(LocalDateTime.now().plusDays(30));
        course.setProgress(BigDecimal.ZERO);
        return course;
    }
}
