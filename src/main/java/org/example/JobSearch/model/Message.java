package org.example.JobSearch.model;

import lombok.*;
import jakarta.persistence.*;
import org.example.JobSearch.converter.AccountTypeConverter;

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
    @JoinColumn(name = "responded_applicants", nullable = false,
            foreignKey = @ForeignKey(name = "fk_responded_applicants"))
    private RespondedApplicant respondedApplicant;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "send_time", nullable = false)
    private Timestamp sendTime;

    @Column(name = "sender_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType senderType;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
}