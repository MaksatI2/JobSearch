package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditUserDTO;

public interface EditUserService {
    void updateUserByEmail(String email, EditUserDTO editUserDTO);
    void deleteUserByEmail(String email);
}