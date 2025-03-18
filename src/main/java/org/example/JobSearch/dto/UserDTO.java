package org.example.JobSearch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
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