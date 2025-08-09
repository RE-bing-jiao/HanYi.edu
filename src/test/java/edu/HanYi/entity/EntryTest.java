package edu.HanYi.entity;

import edu.HanYi.model.Course;
import edu.HanYi.model.Entry;
import edu.HanYi.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Entry validEntry;

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
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testuser");
        mockUser.setPassword("password");
        mockUser.setEmail("emailTEST@mail.ru");
        mockUser.setRole(User.Role.STUDENT);

        Course mockCourse = new Course();
        mockCourse.setId(1);
        mockCourse.setHeader("Header");
        mockCourse.setDescription("Description");

        validEntry = new Entry();
        validEntry.setId(1);
        validEntry.setUser(mockUser);
        validEntry.setCourse(mockCourse);
        validEntry.setEntryDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEntryDateIsNull_thenViolation() {
        validEntry.setEntryDate(null);
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }

    @Test
    void whenEntryDateIsPast_thenViolation() {
        validEntry.setEntryDate(LocalDateTime.now().minusDays(1));
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertFalse(violations.isEmpty());
        assertEquals("должно содержать сегодняшнее число или дату, которая еще не наступила",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenEntryDateIsPresent_thenNoViolation() {
        validEntry.setEntryDate(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEntryDateIsFuture_thenNoViolation() {
        validEntry.setEntryDate(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenUserIsNull_thenViolation() {
        validEntry.setUser(null);
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }

    @Test
    void whenCourseIsNull_thenViolation() {
        validEntry.setCourse(null);
        Set<ConstraintViolation<Entry>> violations = validator.validate(validEntry);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }
}