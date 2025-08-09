package edu.HanYi.repository;

import edu.HanYi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUsernameShouldReturnUserWhenExists() {
        User user = createValidUser("testuser", "test@mail.ru", User.Role.STUDENT);
        entityManager.persist(user);

        Optional<User> found = userRepository.findByUsername("testuser");

        assertTrue(found.isPresent());
        assertEquals("test@mail.ru", found.get().getUsername());
    }

    @Test
    void findByEmailShouldReturnUserWhenExists() {
        User user = createValidUser("testuser", "test@mail.ru", User.Role.STUDENT);
        entityManager.persist(user);

        Optional<User> found = userRepository.findByEmail("test@mail.ru");

        assertTrue(found.isPresent());
        assertEquals("test@mail.ru", found.get().getEmail());
    }

    @Test
    void existsByUsernameShouldReturnTrueWhenUsernameExists() {
        User user = createValidUser("existinguser", "user@mail.ru", User.Role.TEACHER);
        entityManager.persist(user);

        boolean exists = userRepository.existsByUsername("existinguser");

        assertTrue(exists);
    }

    @Test
    void existsByEmailShouldReturnTrueWhenEmailExists() {
        User user = createValidUser("user1", "existing@mail.ru", User.Role.ADMIN);
        entityManager.persist(user);

        assertTrue(userRepository.existsByEmail("existing@mail.ru"));
    }

    @Test
    void findByUsernameShouldReturnEmptyWhenNotExists() {
        Optional<User> found = userRepository.findByUsername("nonexistent");

        assertFalse(found.isPresent());
    }

    @Test
    void findByEmailShouldReturnEmptyWhenNotExists() {
        Optional<User> found = userRepository.findByEmail("nonexistent@mail.ru");

        assertFalse(found.isPresent());
    }

    private User createValidUser(String username, String email, User.Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("validPassword");
        user.setEmail(email);
        user.setRole(role);
        return user;
    }
}