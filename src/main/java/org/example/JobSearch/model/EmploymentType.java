package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employment_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}