package edu.HanYi.entity;

import edu.HanYi.model.Category;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Category validCategory;

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
        validCategory = new Category();
        validCategory.setId(1);
        validCategory.setName("Name");
        validCategory.setDescription("Valid description");
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<Category>> violations = validator.validate(validCategory);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameIsBlank_thenViolation() {
        validCategory.setName("");
        Set<ConstraintViolation<Category>> violations = validator.validate(validCategory);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenNameTooLong_thenViolation() {
        validCategory.setName("This category name is way too long and exceeds 25 characters");
        Set<ConstraintViolation<Category>> violations = validator.validate(validCategory);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 25",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionIsBlank_thenViolation() {
        validCategory.setDescription("");
        Set<ConstraintViolation<Category>> violations = validator.validate(validCategory);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenDescriptionTooLong_thenViolation() {
        String longDescription = "a".repeat(126);
        validCategory.setDescription(longDescription);
        Set<ConstraintViolation<Category>> violations = validator.validate(validCategory);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 125",
                violations.iterator().next().getMessage());
    }
}