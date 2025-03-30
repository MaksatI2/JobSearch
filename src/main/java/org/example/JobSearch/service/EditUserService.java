package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditDTO.EditUserDTO;

public interface EditUserService {
    void updateUserByEmail(String email, EditUserDTO editUserDTO);
    void deleteUserByEmail(String email);
}