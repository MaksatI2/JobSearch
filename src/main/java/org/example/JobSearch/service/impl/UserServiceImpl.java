package org.example.JobSearch.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.service.EmailService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.util.FileUtil;
import org.example.JobSearch.util.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import static org.example.JobSearch.util.FileUtil.DEFAULT_AVATAR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public User getUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с ID: " + id));
    }

    @Override
    public UserDTO findApplicant(String email) {
        User user = userRepository.findByEmailAndType(email, AccountType.APPLICANT)
                .orElseThrow(() -> new UserNotFoundException("Соискатель с email не найден: " + email));
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO findApplicantByPhone(String phoneNumber) {
        User user = userRepository.findByPhoneAndType(phoneNumber, AccountType.APPLICANT)
                .orElseThrow(() -> new UserNotFoundException("Соискатель с телефоном не найден: " + phoneNumber));
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> findApplicantsByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidUserDataException("Имя не может быть пустым");
        }
        List<User> users = userRepository.findByNameAndType(name, AccountType.APPLICANT);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Соискатели с именем не найдены: " + name);
        }
        return users.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public UserDTO findEmployer(String email) {
        User user = userRepository.findByEmailAndType(email, AccountType.EMPLOYER)
                .orElseThrow(() -> new UserNotFoundException("Работодатель с email не найден: " + email));
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO findEmployerByPhone(String phoneNumber) {
        User user = userRepository.findByPhoneAndType(phoneNumber, AccountType.EMPLOYER)
                .orElseThrow(() -> new UserNotFoundException("Работодатель с телефоном не найден: " + phoneNumber));
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> findEmployersByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidUserDataException("Имя не может быть пустым");
        }
        List<User> users = userRepository.findByNameAndType(name, AccountType.EMPLOYER);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Работодатели с именем не найдены: " + name);
        }
        return users.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public void registerApplicant(ApplicantRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidRegisterException("email", "Email уже используется");
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new InvalidRegisterException("phoneNumber", "Номер телефона уже используется");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setAge(dto.getAge());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAvatar(DEFAULT_AVATAR);
        user.setAccountType(AccountType.APPLICANT);

        userRepository.save(user);
    }

    @Override
    public void registerEmployer(EmployerRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidRegisterException("email", "Email уже используется");
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new InvalidRegisterException("phoneNumber", "Номер телефона уже используется");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getCompanyName());
        user.setSurname("");
        user.setAge(0);
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAvatar(DEFAULT_AVATAR);
        user.setAccountType(AccountType.EMPLOYER);

        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getApplicantsVacancy(Long vacancyId) {
        if (vacancyId == null) {
            throw new InvalidUserDataException("ID вакансии не может быть null");
        }
        List<User> applicants = userRepository.findApplicantsByVacancyId(vacancyId);
        if (applicants.isEmpty()) {
            throw new UserNotFoundException("Не найдено соискателей для вакансии ID: " + vacancyId);
        }
        return applicants.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public Page<UserDTO> getAllEmployers(Pageable pageable) {
        Page<User> employers = userRepository.findAllByAccountType(AccountType.EMPLOYER, pageable);
        return employers.map(this::convertToUserDTO);
    }

    @Override
    public ResponseEntity<?> getAvatarByUserId(Long userId) {
        String avatarPath = userRepository.findById(userId)
                .map(User::getAvatar)
                .orElse(DEFAULT_AVATAR);
        return FileUtil.getOutputFile(avatarPath, MediaType.IMAGE_JPEG);
    }

    @Override
    public Long getUserId(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("Нету пользователя с таким Email"));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToUserDTO)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + userId));
    }

    private UserDTO convertToUserDTO(User user) {
        String avatarUrl = user.getAvatar() == null || user.getAvatar().isEmpty() || user.getAvatar().equals(DEFAULT_AVATAR)
                ? "/api/users/avatar/default"
                : "/api/users/avatar/" + user.getId();

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .avatar(avatarUrl)
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Не удалось найти ни одного пользователя с таким адресом электронной почты " + email));
        user.setResetPasswordToken(token);
        userRepository.saveAndFlush(user);
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void makeResetPasswdLink(HttpServletRequest request) throws UserNotFoundException {
        String email = request.getParameter("email");
        String token = UUID.randomUUID().toString();

        try {
            updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/auth/reset_password?token=" + token;
            emailService.sendEmail(email, resetPasswordLink);
        } catch (UserNotFoundException ex) {
            throw ex;
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException("Ошибка при отправке электронного письма", e);
        }
    }
}
