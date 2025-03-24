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
        if (userDto.getAge() != null && userDto.getAge() < 18) {
            throw new InvalidUserDataException("Age must be at least 18");
        }
        if (userDto.getPhoneNumber() != null && userDto.getPhoneNumber().length() < 10) {
            throw new InvalidUserDataException("Phone number must contain at least 10 digits");
        }
        if (userDto.getAccountType() == null ||
                (!userDto.getAccountType().equalsIgnoreCase("EMPLOYER") &&
                        !userDto.getAccountType().equalsIgnoreCase("APPLICANT"))) {
            throw new InvalidUserDataException("Invalid account type");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAvatar(userDto.getAvatar());
        user.setAccountType(userDto.getAccountType());

        userDao.save(user);

        System.out.println("User registered successfully: " + user.getEmail());
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

        System.out.println("User " + user.getEmail() + " logged in successfully");
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
