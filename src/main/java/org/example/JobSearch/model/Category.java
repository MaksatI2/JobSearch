package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_categories_parent"))
    private Category parent;
}
