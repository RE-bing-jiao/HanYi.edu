package edu.HanYi.repository;

import edu.HanYi.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourseId(Integer courseId);
    boolean existsByHeaderAndCourseId(String header, Integer courseId);
}