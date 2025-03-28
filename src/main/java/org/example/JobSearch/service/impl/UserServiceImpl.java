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

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAvatar(userDto.getAvatar());
        user.setAccountType(userDto.getAccountType());

        validateUser(userDto);

        userDao.save(user);
    }

    @Override
    public void login(UserDTO userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new InvalidUserDataException("Email cannot be empty");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be empty");
        }

        User user = userDao.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!userDto.getPassword().equals(user.getPassword())) {
            throw new InvalidUserDataException("Invalid password");
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

    @Override
    public void updateUserAvatar(Long userId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidUserDataException("File cannot be empty");
        }

        userDao.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        try {
            String avatarPath = FileUtil.saveUploadFile(file, "images/");

            int updatedRows = userDao.updateUserAvatar(userId, avatarPath);

            if (updatedRows == 0) {
                throw new RuntimeException("Failed to update avatar for user with id: " + userId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update avatar: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvatarByUserId(Long userId) {
        if (userId == null) {
            throw new InvalidUserDataException("User ID cannot be null");
        }

        String avatarPath = userDao.findAvatarPathById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found or has no avatar"));

        if (avatarPath == null || avatarPath.isEmpty()) {
            throw new UserNotFoundException("User has no avatar");
        }

        try {
            String imageName = avatarPath.substring(avatarPath.lastIndexOf('/') + 1);
            return FileUtil.getOutputFile(imageName, "images/", MediaType.IMAGE_JPEG);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve avatar: " + e.getMessage());
        }
    }

    private void validateUser(UserDTO userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new InvalidUserDataException("Email cannot be empty");
        }
        if (userDao.existsByEmail(userDto.getEmail())) {
            throw new InvalidUserDataException("Email is already in use");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be empty");
        }
        if (userDto.getPassword().length() < 6) {
            throw new InvalidUserDataException("Password must be at least 6 characters");
        }
        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            throw new InvalidUserDataException("Name cannot be empty");
        }
        if (userDto.getSurname() == null || userDto.getSurname().trim().isEmpty()) {
            throw new InvalidUserDataException("Surname cannot be empty");
        }
        if (userDto.getAge() == null) {
            throw new InvalidUserDataException("Age cannot be null");
        }
        if (userDto.getAge() < 18 || userDto.getAge() > 60) {
            throw new InvalidUserDataException("Age must be between 18 and 60");
        }
        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().matches("^\\d{10,15}$")) {
            throw new InvalidUserDataException("Phone number must contain only digits and be at least 10 characters long");
        }
        if (userDto.getAccountType() == null ||
                (!userDto.getAccountType().equalsIgnoreCase("EMPLOYER") &&
                        !userDto.getAccountType().equalsIgnoreCase("APPLICANT"))) {
            throw new InvalidUserDataException("Invalid account type");
        }
        if (!userDto.getName().matches("^[a-zA-Zа-яА-Я]+$")) {
            throw new InvalidUserDataException("Name cannot contain numbers or special characters");
        }
        if (!userDto.getSurname().matches("^[a-zA-Zа-яА-Я]+$")) {
            throw new InvalidUserDataException("Surname cannot contain numbers or special characters");
        }
    }

}
