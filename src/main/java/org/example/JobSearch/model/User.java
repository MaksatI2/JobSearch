package org.example.JobSearch.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private Integer age;
    private String password;
    private String phoneNumber;
    private String avatar;
    private String accountType;
}

