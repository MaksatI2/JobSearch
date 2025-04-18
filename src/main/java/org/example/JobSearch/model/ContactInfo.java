package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_info")
@Getter
@Setter
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_contact_info_resume"))
    private Resume resume;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_contact_info_type"))
    private ContactType type;

    @Column(name = "text", nullable = false, length = 255)
    private String value;
}