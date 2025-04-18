package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_types")
@Getter
@Setter
public class ContactType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;
}