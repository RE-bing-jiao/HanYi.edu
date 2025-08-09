package edu.HanYi.entity;

import edu.HanYi.model.Category;
import edu.HanYi.model.Course;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Course validCourse;

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
        Category mockCategory = new Category();
        mockCategory.setId(1);
        mockCategory.setName("Valid name");

        validCourse = new Course();
        validCourse.setId(1);
        validCourse.setHeader("Valid Course Header");
        validCourse.setDescription("Valid description");
        validCourse.setCategory(mockCategory);
        validCourse.setEntryDate(LocalDateTime.now().plusDays(1));
        validCourse.setExitDate(LocalDateTime.now().plusDays(30));
        validCourse.setProgress(BigDecimal.ZERO);
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenHeaderIsBlank_thenViolation() {
        validCourse.setHeader("");
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenHeaderTooLong_thenViolation() {
        validCourse.setHeader("This header is way too long and exceeds 25 characters");
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 25",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionIsBlank_thenViolation() {
        validCourse.setDescription("");
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionTooLong_thenViolation() {
        String longDesc = "a".repeat(126);
        validCourse.setDescription(longDesc);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 125",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenEntryDateIsNull_thenViolation() {
        validCourse.setEntryDate(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }

    @Test
    void whenEntryDateIsPast_thenViolation() {
        validCourse.setEntryDate(LocalDateTime.now().minusDays(1));
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("должно содержать сегодняшнее число или дату, которая еще не наступила",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenExitDateIsNull_thenViolation() {
        validCourse.setExitDate(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }

    @Test
    void whenExitDateIsPast_thenViolation() {
        validCourse.setExitDate(LocalDateTime.now().minusDays(1));
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("должно содержать сегодняшнее число или дату, которая еще не наступила",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenProgressIsNegative_thenViolation() {
        validCourse.setProgress(new BigDecimal("-0.01"));
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("должно быть больше, чем или равно 0.00",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenProgressExceeds100_thenViolation() {
        validCourse.setProgress(new BigDecimal("100.01"));
        Set<ConstraintViolation<Course>> violations = validator.validate(validCourse);
        assertFalse(violations.isEmpty());
        assertEquals("должно быть меньше, чем или равно 100.00",
                violations.iterator().next().getMessage());
    }
}
