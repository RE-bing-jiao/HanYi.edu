package edu.HanYi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CourseResponse(
        Integer id,
        String header,
        String description,
        Integer categoryId,
        String categoryName,
        LocalDateTime entryDate,
        LocalDateTime exitDate,
        BigDecimal progress,
        List<LessonResponse> lessons,
        List<EntryResponse> entries
) {
    public record LessonResponse(
            Integer id,
            Integer lessonOrderNum,
            String header
    ){}

    public record EntryResponse(
            Integer id,
            LocalDateTime entryDate,
            Integer userId
    ){}
}
