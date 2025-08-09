package edu.HanYi.entity;

import edu.HanYi.model.ContactRequest;
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

public class ContactRequestTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private ContactRequest validContactRequest;

    @BeforeAll
    static void setUpAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if(validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        validContactRequest = new ContactRequest();
        validContactRequest.setId(1);
        validContactRequest.setName("name");
        validContactRequest.setEmail("TEST@mail.ru");
        validContactRequest.setName("msg");
    }

    @Test
    void whenAllFieldsValid_thenNoViolations(){
        Set<ConstraintViolation<ContactRequest>> violations = validator.validate(validContactRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenNameIsBlank_ThenViolation(){
        validContactRequest.setName("");
        Set<ConstraintViolation<ContactRequest>> violations = validator.validate(validContactRequest);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenEmailIsBlank_ThenViolation(){
        validContactRequest.setEmail("");
        Set<ConstraintViolation<ContactRequest>> violations = validator.validate(validContactRequest);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenEmailIsInvalid_thenViolation() {
        validContactRequest.setEmail("invalid-email");
        Set<ConstraintViolation<ContactRequest>> violations = validator.validate(validContactRequest);
        assertFalse(violations.isEmpty());
        assertEquals("должно иметь формат адреса электронной почты", violations.iterator().next().getMessage());
    }

    @Test
    void whenMessageIsNull_thenNoViolation() {
        validContactRequest.setMessage(null);
        Set<ConstraintViolation<ContactRequest>> violations = validator.validate(validContactRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenMessageIsEmpty_thenNoViolation() {
        validContactRequest.setMessage("");
        Set<ConstraintViolation<ContactRequest>> violations = validator.validate(validContactRequest);
        assertTrue(violations.isEmpty());
    }
}