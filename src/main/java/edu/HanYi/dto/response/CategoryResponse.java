package edu.HanYi.dto.response;

import java.util.List;

public record CategoryResponse(
        Integer id,
        String name,
        String description,
        List<CourseResponse> courses
){
    public record CourseResponse(
            Integer id,
            String header
    ){}
}