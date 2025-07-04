package edu.HanYi.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false)
    private String header;

    @NotBlank
    @Size(max = 125)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("course-entries")
    private List<Entry> entries = new ArrayList<>();

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime entryDate;

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDateTime exitDate;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;
}