package edu.HanYi.entity;

import edu.HanYi.model.Flashcard;
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

class FlashcardTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private Flashcard validFlashcard;

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
        validFlashcard = new Flashcard();
        validFlashcard.setId(1);
        validFlashcard.setFrontText("Valid front text");
        validFlashcard.setBackText("Valid back text");
        validFlashcard.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<Flashcard>> violations = validator.validate(validFlashcard);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenFrontTextIsBlank_thenViolation() {
        validFlashcard.setFrontText("");
        Set<ConstraintViolation<Flashcard>> violations = validator.validate(validFlashcard);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenFrontTextTooLong_thenViolation() {
        validFlashcard.setFrontText("This front text is way too long and exceeds 25 characters");
        Set<ConstraintViolation<Flashcard>> violations = validator.validate(validFlashcard);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 25", violations.iterator().next().getMessage());
    }

    @Test
    void whenBackTextIsBlank_thenViolation() {
        validFlashcard.setBackText("");
        Set<ConstraintViolation<Flashcard>> violations = validator.validate(validFlashcard);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenBackTextTooLong_thenViolation() {
        validFlashcard.setBackText("This back text is way too long and exceeds 35 characters limit");
        Set<ConstraintViolation<Flashcard>> violations = validator.validate(validFlashcard);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 0 до 35", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreatedAtIsNull_thenViolation() {
        validFlashcard.setCreatedAt(null);
        Set<ConstraintViolation<Flashcard>> violations = validator.validate(validFlashcard);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }
}