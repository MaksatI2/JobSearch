package org.example.JobSearch.model;

import lombok.*;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "responded_applicants_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_responded_applicants"))
    private RespondedApplicant respondedApplicant;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "send_time", nullable = false)
    private Timestamp timestamp;
}