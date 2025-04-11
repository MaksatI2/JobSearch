package org.example.JobSearch.service;

import org.example.JobSearch.dto.EditDTO.EditUserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EditUserService {
    void updateUserByEmail(String email, EditUserDTO editUserDTO);
    void updateUserAvatar(Long userId, MultipartFile file);
}