package edu.HanYi.repository;

import edu.HanYi.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer> {
    List<Entry> findByUserId(Integer userId);
    List<Entry> findByCourseId(Integer courseId);
    boolean existsByUserIdAndCourseId(Integer userId, Integer courseId);
}
