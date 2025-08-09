package edu.HanYi.entity;

import edu.HanYi.model.User;
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

class UserTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private User validUser;

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
        validUser = new User();
        validUser.setId(1);
        validUser.setUsername("validUsername");
        validUser.setPassword("validPassword");
        validUser.setEmail("emailTEST@mail.ru");
        validUser.setRole(User.Role.STUDENT);
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenUsernameIsBlank_thenViolation() {
        validUser.setUsername("");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("не должно быть пустым") ||
                        v.getMessage().equals("размер должен находиться в диапазоне от 5 до 25")));
    }

    @Test
    void whenUsernameTooShort_thenViolation() {
        validUser.setUsername("abc");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 5 до 25",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenUsernameTooLong_thenViolation() {
        validUser.setUsername("thisusernameiswaaaytoolongforvalidation");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
        assertEquals("размер должен находиться в диапазоне от 5 до 25",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenPasswordIsBlank_thenViolation() {
        validUser.setPassword("");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenEmailIsBlank_thenViolation() {
        validUser.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
        assertEquals("не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void whenEmailInvalidFormat_thenViolation() {
        validUser.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
        assertEquals("должно иметь формат адреса электронной почты",
                violations.iterator().next().getMessage());
    }

    @Test
    void whenEmailTooLong_thenViolation() {
        validUser.setEmail("thisisareallylongemailaddressthatiswaaaybeyondthethirtyfivecharacterlimit@mail.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("не должно быть пустым") ||
                        v.getMessage().equals("должно иметь формат адреса электронной почты")));
    }

    @Test
    void whenRoleIsNull_thenViolation() {
        validUser.setRole(null);
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
        assertEquals("не должно равняться null", violations.iterator().next().getMessage());
    }
}