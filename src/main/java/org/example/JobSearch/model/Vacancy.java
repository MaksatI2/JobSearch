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

    @Column(nullable = false, length = 255)
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    private Float salary;

    @Column(name = "exp_from")
    private Integer expFrom;

    @Column(name = "exp_to")
    private Integer expTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "region_id",
            foreignKey = @ForeignKey(name = "fk_vacancies_region"))
    private Region region;

    @ManyToMany
    @JoinTable(
            name = "vacancy_employment_types",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "employment_type_id"),
            foreignKey = @ForeignKey(name = "fk_vacancy_employment_vacancy"),
            inverseForeignKey = @ForeignKey(name = "fk_vacancy_employment_type"))
    private List<EmploymentType> employmentTypes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "vacancy_work_schedules",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "work_schedule_id"),
            foreignKey = @ForeignKey(name = "fk_vacancy_schedule_vacancy"),
            inverseForeignKey = @ForeignKey(name = "fk_vacancy_schedule_type"))
    private List<WorkSchedule> workSchedules = new ArrayList<>();

    @Column(name = "created_date", nullable = false, updatable = false)
    private Timestamp createdDate;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @OneToMany(mappedBy = "vacancy")
    private List<RespondedApplicant> applicants = new ArrayList<>();
}

