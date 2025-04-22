package org.example.JobSearch.service;

import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    void  registerEmployer(EmployerRegisterDTO employerDto);
    void  registerApplicant(ApplicantRegisterDTO applicantDto);

    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);
    UserDTO findApplicant(String email);
    UserDTO findApplicantByPhone(String phoneNumber);
    UserDTO findEmployer(String email);
    UserDTO findEmployerByPhone(String phoneNumber);

    List<UserDTO> findApplicantsByName(String name);
    List<UserDTO> findEmployersByName(String name);
    List<UserDTO> getApplicantsVacancy(Long vacancyId);
    List<UserDTO> getAllEmployers();

    ResponseEntity<?> getAvatarByUserId(Long userId);
    boolean userExists(String email);
    Long getUserId(String email);
    User getUserId(Long id);
}
