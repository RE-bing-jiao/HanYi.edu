package edu.HanYi.repository;

import edu.HanYi.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByCategoryId(Integer categoryId);
    boolean existsByHeader(String header);
}