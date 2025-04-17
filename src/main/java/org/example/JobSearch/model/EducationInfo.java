package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "education_info")
@Getter
@Setter
public class EducationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_education_info_resume"))
    private Resume resume;

    @Column(name = "institution_name", nullable = false, length = 255)
    private String institution;

    @Column(length = 255)
    private String program;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(length = 255)
    private String degree;
}