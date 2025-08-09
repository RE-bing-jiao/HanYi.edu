package edu.HanYi.repository;

import edu.HanYi.model.Category;
import edu.HanYi.model.Course;

import edu.HanYi.model.Lesson;
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
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Name");
        category.setDescription("Description");
        entityManager.persist(category);

        testCourse = new Course();
        testCourse.setHeader("Header");
        testCourse.setDescription("Description");
        testCourse.setCategory(category);
        testCourse.setEntryDate(LocalDateTime.now().plusDays(1));
        testCourse.setExitDate(LocalDateTime.now().plusDays(30));
        entityManager.persist(testCourse);
    }

    @Test
    void findByCourseIdShouldReturnLessonsWhenCourseExists() {
        Lesson lesson = createValidLesson("Lesson", testCourse);
        lessonRepository.save(lesson);
        List<Lesson> foundLessons = lessonRepository.findByCourseId(testCourse.getId());

        assertFalse(foundLessons.isEmpty());
        assertEquals(1, foundLessons.size());
        assertEquals(testCourse.getId(), foundLessons.get(0).getCourse().getId());
    }

    @Test
    void findByCourseIdShouldReturnEmptyListWhenCourseHasNoLessons() {
        List<Lesson> foundLessons = lessonRepository.findByCourseId(testCourse.getId());

        assertTrue(foundLessons.isEmpty());
    }

    @Test
    void existsByHeaderAndCourseIdShouldReturnTrueWhenLessonExists() {
        Lesson lesson = createValidLesson("Header", testCourse);
        lessonRepository.save(lesson);

        assertTrue(lessonRepository.existsByHeaderAndCourseId("Header", testCourse.getId()));
    }

    @Test
    void existsByHeaderAndCourseIdShouldReturnFalseWhenLessonDoesNotExist() {
        assertFalse(lessonRepository.existsByHeaderAndCourseId("Non-existent Header", testCourse.getId()));
    }

    private Lesson createValidLesson(String header, Course course) {
        Lesson lesson = new Lesson();
        lesson.setLessonOrderNum(1);
        lesson.setHeader(header);
        lesson.setDescription("Description");
        lesson.setUrl("Url");
        lesson.setCourse(course);
        return lesson;
    }
}
