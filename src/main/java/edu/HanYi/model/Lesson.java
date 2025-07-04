package edu.HanYi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "lesson_order_num", nullable = false)
    private Integer lessonOrderNum;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false)
    private String header;

    @NotBlank
    @Size(max = 125)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank
    @Size(max = 125)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;
}