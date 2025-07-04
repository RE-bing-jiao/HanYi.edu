package edu.HanYi.dto.response;

public record LessonResponse(
        Integer id,
        Integer lessonOrderNum,
        String header,
        String description,
        String url,
        Integer courseId,
        String courseHeader
) {}