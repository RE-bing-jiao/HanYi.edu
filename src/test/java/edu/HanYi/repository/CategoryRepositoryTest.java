package edu.HanYi.repository;

import edu.HanYi.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void existsByNameShouldReturnTrueWhenNameExists() {
        Category category = new Category();
        category.setName("Name");
        category.setDescription("Description");
        categoryRepository.save(category);

        assertTrue(categoryRepository.existsByName("Name"));
    }

    @Test
    void existsByNameShouldReturnFalseWhenNameDoesNotExist() {
        assertFalse(categoryRepository.existsByName("NonExisting"));
    }

    @Test
    void findByNameShouldReturnCategoryWhenNameExists() {
        Category category = new Category();
        category.setName("Name");
        category.setDescription("Description");
        categoryRepository.save(category);

        Optional<Category> found = categoryRepository.findByName("Name");
        assertTrue(found.isPresent());
        assertEquals("Name", found.get().getName());
    }

    @Test
    void findByNameShouldReturnEmptyOptionalWhenNameDoesNotExist() {
        Optional<Category> found = categoryRepository.findByName("NonExisting");
        assertFalse(found.isPresent());
    }
}