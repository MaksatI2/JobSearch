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

import java.util.List;

public interface UserService {

    void  registerEmployer(EmployerRegisterDTO employerDto);
    void  registerApplicant(ApplicantRegisterDTO applicantDto);

    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);
    ResponseEntity<?> getAvatarByUserId(Long userId);

    Page<UserDTO> getAllEmployers(Pageable pageable);

    Long getUserId(String email);
    User getUserId(Long id);

    void updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    void makeResetPasswdLink(HttpServletRequest request) throws UserNotFoundException;
}
