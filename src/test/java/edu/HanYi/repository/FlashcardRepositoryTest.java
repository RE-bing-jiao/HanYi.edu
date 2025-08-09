package edu.HanYi.repository;

import edu.HanYi.model.Flashcard;
import edu.HanYi.model.User;
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
public class FlashcardRepositoryTest {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole(User.Role.STUDENT);
        entityManager.persist(testUser);
    }

    @Test
    void findByUserIdShouldReturnFlashcardsWhenUserExists() {
        Flashcard flashcard = createValidFlashcard(testUser);
        flashcardRepository.save(flashcard);
        List<Flashcard> flashcards = flashcardRepository.findByUserId(testUser.getId());

        assertFalse(flashcards.isEmpty());
        assertEquals(1, flashcards.size());
        assertEquals(testUser.getId(), flashcards.get(0).getUser().getId());
    }

    @Test
    void findByUserIdShouldReturnEmptyListWhenUserHasNoFlashcards() {
        List<Flashcard> flashcards = flashcardRepository.findByUserId(testUser.getId());
        assertTrue(flashcards.isEmpty());
    }

    private Flashcard createValidFlashcard(User user) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText("Front");
        flashcard.setBackText("back");
        flashcard.setCreatedAt(LocalDateTime.now());
        flashcard.setUser(user);
        return flashcard;

    }
}
