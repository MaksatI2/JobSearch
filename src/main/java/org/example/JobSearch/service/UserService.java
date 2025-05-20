package org.example.JobSearch.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    void  registerEmployer(EmployerRegisterDTO employerDto);
    void  registerApplicant(ApplicantRegisterDTO applicantDto);
    void updatePassword(User user, String newPassword);
    void updateResetPasswordToken(String token, String email);
    void makeResetPasswdLink(HttpServletRequest request) throws UserNotFoundException;

    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);

    ResponseEntity<?> getAvatarByUserId(Long userId);
    Page<UserDTO> getAllEmployers(Pageable pageable);

    Long getUserId(String email);

    boolean isEmployer(String userEmail);

    User getUserId(Long id);
    User getByResetPasswordToken(String token);
}
