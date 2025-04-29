package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_resumes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_fav_resume_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resume_id", foreignKey = @ForeignKey(name = "fk_fav_resume"))
    private Resume resume;
}