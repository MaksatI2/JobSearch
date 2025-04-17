package org.example.JobSearch.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "work_experience_info")
@Getter
@Setter
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_work_experience_resume"))
    private Resume resume;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(nullable = false, length = 255)
    private String position;

    private Integer years;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;
}
