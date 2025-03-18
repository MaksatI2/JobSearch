package org.example.JobSearch.service;

import org.example.JobSearch.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void findApplicant(Integer userId);
    void findEmployer(Integer userId);

    void register(UserDTO userDto);

    void login(UserDTO userDto);

    String addAvatar(MultipartFile file);

    ResponseEntity<?> getAvatarByName(String imageName);
}
