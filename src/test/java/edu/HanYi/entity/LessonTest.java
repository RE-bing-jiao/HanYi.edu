package edu.HanYi.entity;

import edu.HanYi.model.Lesson;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LessonTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Lesson validLesson;

    @BeforeAll
    static void setUpAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDownAll() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        validLesson = new Lesson();
        validLesson.setId(1);
        validLesson.setLessonOrderNum(1);
        validLesson.setHeader("Header");
        validLesson.setDescription("Description");
        validLesson.setUrl("url");
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenLessonOrderNumIsNull_thenViolation() {
        validLesson.setLessonOrderNum(null);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeaderIsBlank_thenViolation() {
        validLesson.setHeader("");
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeaderTooLong_thenViolation() {
        validLesson.setHeader("This header is way too long and exceeds 25 characters limit");
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 25",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionIsBlank_thenViolation() {
        validLesson.setDescription("");
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionTooLong_thenViolation() {
        String longDescription = "a".repeat(126);
        validLesson.setDescription(longDescription);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 125",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenUrlIsBlank_thenViolation() {
        validLesson.setUrl("");
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenUrlTooLong_thenViolation() {
        String longUrl = "a".repeat(126);
        validLesson.setUrl(longUrl);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(validLesson);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 125",
                violations.iterator().next().getMessage());
    }
}
