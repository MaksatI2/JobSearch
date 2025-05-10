package org.example.JobSearch.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.common.Utility;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.service.EmailService;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.util.FileUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.example.JobSearch.util.FileUtil.DEFAULT_AVATAR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public User getUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(getMessage("user.not.found.with.id ") + id));
    }
    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(getMessage("user.not.found")));
        return convertToUserDTO(user);
    }

    @Override
    public void registerApplicant(ApplicantRegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new InvalidRegisterException("email", getMessage("user.email.already.used"));
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new InvalidRegisterException("phoneNumber", getMessage("user.phone.already.used"));
        }

        String lang = LocaleContextHolder.getLocale().getLanguage();

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setLanguage(lang);
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
            throw new InvalidRegisterException("email",  getMessage("user.email.already.used"));
        }
        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new InvalidRegisterException("phoneNumber",  getMessage("user.phone.already.used"));
        }
        String lang = LocaleContextHolder.getLocale().getLanguage();

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setLanguage(lang);
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
                .orElseThrow(() -> new UserNotFoundException( getMessage("user.not.found.with.email")));
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::convertToUserDTO)
                .orElseThrow(() -> new UserNotFoundException( getMessage("user.not.found ") + userId));
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
                .orElseThrow(() -> new UserNotFoundException( getMessage("user.not.found.for.reset ") + email));
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
            throw new RuntimeException( getMessage("reset.email.error"));
        }
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
