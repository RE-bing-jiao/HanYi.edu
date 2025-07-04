package edu.HanYi.dto.response;

import java.time.LocalDateTime;

public record EntryResponse(
        Integer id,
        Integer userId,
        String username,
        Integer courseId,
        String courseHeader,
        LocalDateTime entryDate
) {}
