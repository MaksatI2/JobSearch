package org.example.JobSearch.model;

import lombok.*;
import jakarta.persistence.*;
import org.example.JobSearch.converter.AccountTypeConverter;

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

    @Convert(converter = AccountTypeConverter.class)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private Boolean enabled = true;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacancy> vacancies = new ArrayList<>();
}