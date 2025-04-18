package org.example.JobSearch.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "responded_applicants")
@Getter
@Setter
public class RespondedApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_responded_vacancy"))
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_responded_resume"))
    private Resume resume;

    @Column(nullable = false)
    private Boolean confirmation = false;

    @OneToMany(mappedBy = "respondedApplicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();
}
