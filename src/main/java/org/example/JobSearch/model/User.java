package org.example.JobSearch.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String surname;

    private Integer age;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String avatar;

    @Column(name = "account_type")
    private Integer accountType;

    @Column(nullable = false)
    private Boolean enabled = true;

    @OneToMany(mappedBy = "applicant")
    private List<Resume> resumes = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Vacancy> vacancies = new ArrayList<>();
}

