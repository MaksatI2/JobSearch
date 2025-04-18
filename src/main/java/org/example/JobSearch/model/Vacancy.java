package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_vacancies_author"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_vacancies_category"))
    private Category category;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private Float salary;

    @Column(name = "exp_from")
    private Integer expFrom;

    @Column(name = "exp_to")
    private Integer expTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_date", nullable = false, updatable = false)
    private Timestamp createdDate;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @OneToMany(mappedBy = "vacancy")
    private List<RespondedApplicant> applicants = new ArrayList<>();
}

