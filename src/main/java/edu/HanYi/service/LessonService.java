package edu.HanYi.service;

import edu.HanYi.dto.request.LessonCreateRequest;
import edu.HanYi.dto.response.LessonResponse;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(LessonCreateRequest request);
    List<LessonResponse> getAllLessons();
    LessonResponse getLessonById(Integer id);
    List<LessonResponse> getLessonsByCourseId(Integer courseId);
    LessonResponse updateLesson(Integer id, LessonCreateRequest request);
    void deleteLesson(Integer id);
}