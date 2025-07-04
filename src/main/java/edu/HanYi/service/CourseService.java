package edu.HanYi.service;

import edu.HanYi.dto.request.CourseCreateRequest;
import edu.HanYi.dto.response.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseCreateRequest request);
    List<CourseResponse> getAllCourses();
    CourseResponse getCourseById(Integer id);
    List<CourseResponse> getCoursesByCategoryId(Integer categoryId);
    CourseResponse updateCourse(Integer id, CourseCreateRequest request);
    void deleteCourse(Integer id);
}