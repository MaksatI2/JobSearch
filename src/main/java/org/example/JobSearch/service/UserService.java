package org.example.JobSearch.service;

import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDTO findApplicant(String email);

    UserDTO getUserByEmail(String email);

    UserDTO findApplicantByPhone(String phoneNumber);
    List<UserDTO> findApplicantsByName(String name);

    UserDTO findEmployer(String email);
    UserDTO findEmployerByPhone(String phoneNumber);
    List<UserDTO> findEmployersByName(String name);

    void  registerEmployer(EmployerRegisterDTO employerDto);
    void  registerApplicant(ApplicantRegisterDTO applicantDto);

    ResponseEntity<?> getAvatarByUserId(Long userId);

    List<UserDTO> getApplicantsVacancy(Long vacancyId);

    boolean userExists(String email);
}
