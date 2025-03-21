package org.example.JobSearch.service.impl;

import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.model.User;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.util.FileUtil;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    @Override
    public UserDTO findApplicant(String email) {
        User user = userDao.findApplicant(email)
                .orElseThrow(UserNotFoundException::new);
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO findApplicantByPhone(String phoneNumber) {
        User user = userDao.findApplicantByPhone(phoneNumber)
                .orElseThrow(UserNotFoundException::new);
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> findApplicantsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidUserDataException("Name cannot be empty");
        }
        List<User> users = userDao.findApplicantsByName(name);
        if (users.isEmpty()) {
            throw new UserNotFoundException("No applicants found with name: " + name);
        }
        return users.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public UserDTO findEmployer(String email) {
        User user = userDao.findEmployer(email)
                .orElseThrow(UserNotFoundException::new);
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO findEmployerByPhone(String phoneNumber) {
        User user = userDao.findEmployerByPhone(phoneNumber)
                .orElseThrow(UserNotFoundException::new);
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> findEmployersByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidUserDataException("Name cannot be empty");
        }
        List<User> users = userDao.findEmployersByName(name);
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        return users.stream().map(this::convertToUserDTO).toList();
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .avatar(user.getAvatar())
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    public void register(UserDTO userDto) {
        // TODO: сделать логику для регистрацию пользователя
    }

    @Override
    public void login(UserDTO userDto) {
        // TODO: сделать логику для авторизации пользователя
    }

    @Override
    public String addAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidUserDataException("File cannot be empty");
        }
        try {
            return FileUtil.saveUploadFile(file, "images/");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvatarByName(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            throw new InvalidUserDataException("Image name cannot be empty");
        }
        try {
            return FileUtil.getOutputFile(imageName, "images/", MediaType.IMAGE_JPEG);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file: " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getApplicantsVacancy(Long vacancyId) {
        if (vacancyId == null) {
            throw new InvalidUserDataException("Vacancy ID cannot be null");
        }
        List<User> applicants = userDao.findApplicantsByVacancyId(vacancyId);
        if (applicants.isEmpty()) {
            throw new UserNotFoundException("No applicants found for vacancy ID: " + vacancyId);
        }
        return applicants.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public boolean userExists(String email) {
        return userDao.existsByEmail(email);
    }
}
