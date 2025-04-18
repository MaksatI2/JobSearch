package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resumes_applicant"))
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resumes_category"))
    private Category category;

    @Column(nullable = false, length = 255)
    private String name;

    private Float salary;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Timestamp createDate;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactInfo> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EducationInfo> education = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespondedApplicant> respondedApplicants = new ArrayList<>();
}