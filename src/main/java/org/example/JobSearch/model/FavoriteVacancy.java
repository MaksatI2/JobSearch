package org.example.JobSearch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteVacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_fav_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vacancy_id", foreignKey = @ForeignKey(name = "fk_fav_vacancy"))
    private Vacancy vacancy;
}
