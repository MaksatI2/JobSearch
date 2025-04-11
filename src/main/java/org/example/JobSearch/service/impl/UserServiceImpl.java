package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.dto.register.ApplicantRegisterDTO;
import org.example.JobSearch.dto.register.EmployerRegisterDTO;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.User;
import org.example.JobSearch.service.UserService;
import org.example.JobSearch.util.FileUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.JobSearch.util.FileUtil.DEFAULT_AVATAR;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO findApplicant(String email) {
        log.info("Поиск соискателя по email: {}", email);
        User user = userDao.findApplicant(email)
                .orElseThrow(() -> {
                    log.error("Соискатель с email {} не найден", email);
                    return new UserNotFoundException("Соискатель с email не найден: " + email);
                });
        log.debug("Соискатель с email {} найден", email);
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.info("Получение пользователя по email: {}", email);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Пользователь с email {} не найден", email);
                    return new UserNotFoundException("Пользователь не найден");
                });
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO findApplicantByPhone(String phoneNumber) {
        log.info("Поиск соискателя по телефону: {}", phoneNumber);
        User user = userDao.findApplicantByPhone(phoneNumber)
                .orElseThrow(() -> {
                    log.error("Соискатель с телефоном {} не найден", phoneNumber);
                    return new UserNotFoundException("Соискатель с телефоном не найден: " + phoneNumber);
                });
        log.debug("Соискатель с телефоном {} найден", phoneNumber);
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> findApplicantsByName(String name) {
        log.info("Поиск соискателей по имени: {}", name);
        if (name == null || name.isEmpty()) {
            log.error("Имя для поиска не может быть пустым");
            throw new InvalidUserDataException("Имя не может быть пустым");
        }
        List<User> users = userDao.findApplicantsByName(name);
        if (users.isEmpty()) {
            log.warn("Соискатели с именем {} не найдены", name);
            throw new UserNotFoundException("Соискатели с именем не найдены: " + name);
        }
        log.debug("Найдено {} соискателей с именем {}", users.size(), name);
        return users.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public UserDTO findEmployer(String email) {
        log.info("Поиск работодателя по email: {}", email);
        User user = userDao.findEmployer(email)
                .orElseThrow(() -> {
                    log.error("Работодатель с email {} не найден", email);
                    return new UserNotFoundException("Работодатель с email не найден: " + email);
                });
        log.debug("Работодатель с email {} найден", email);
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO findEmployerByPhone(String phoneNumber) {
        log.info("Поиск работодателя по телефону: {}", phoneNumber);
        User user = userDao.findEmployerByPhone(phoneNumber)
                .orElseThrow(() -> {
                    log.error("Работодатель с телефоном {} не найден", phoneNumber);
                    return new UserNotFoundException("Работодатель с телефоном не найден: " + phoneNumber);
                });
        log.debug("Работодатель с телефоном {} найден", phoneNumber);
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> findEmployersByName(String name) {
        log.info("Поиск работодателей по имени: {}", name);
        if (name == null || name.isEmpty()) {
            log.error("Имя для поиска не может быть пустым");
            throw new InvalidUserDataException("Имя не может быть пустым");
        }
        List<User> users = userDao.findEmployersByName(name);
        if (users.isEmpty()) {
            log.warn("Работодатели с именем {} не найдены", name);
            throw new UserNotFoundException("Работодатели с именем не найдены: " + name);
        }
        log.debug("Найдено {} работодателей с именем {}", users.size(), name);
        return users.stream().map(this::convertToUserDTO).toList();
    }

    private UserDTO convertToUserDTO(User user) {
        log.trace("Конвертация User в UserDTO для пользователя ID: {}", user.getId());

        String avatarUrl = user.getAvatar() == null || user.getAvatar().isEmpty() ||
                user.getAvatar().equals(DEFAULT_AVATAR)
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
    public void registerApplicant(ApplicantRegisterDTO applicantDto) {
        validateApplicantData(applicantDto);

        if (userDao.existsByEmail(applicantDto.getEmail())) {
            throw new InvalidRegisterException("email", "Email уже используется");
        }

        User user = new User();
        user.setEmail(applicantDto.getEmail());
        user.setPassword(passwordEncoder.encode(applicantDto.getPassword()));
        user.setName(applicantDto.getName());
        user.setSurname(applicantDto.getSurname());
        user.setAge(applicantDto.getAge());
        user.setPhoneNumber(applicantDto.getPhoneNumber());
        user.setAvatar(DEFAULT_AVATAR);
        user.setAccountType(AccountType.APPLICANT);

        userDao.save(user);
    }

    @Override
    public void registerEmployer(EmployerRegisterDTO employerDto) {
        validateEmployerData(employerDto);

        if (userDao.existsByEmail(employerDto.getEmail())) {
            throw new InvalidRegisterException("email", "Email уже используется");
        }

        User user = new User();
        user.setEmail(employerDto.getEmail());
        user.setPassword(passwordEncoder.encode(employerDto.getPassword()));
        user.setName(employerDto.getCompanyName());
        user.setSurname("");
        user.setAge(0);
        user.setPhoneNumber(employerDto.getPhoneNumber());
        user.setAvatar(DEFAULT_AVATAR);
        user.setAccountType(AccountType.EMPLOYER);

        userDao.save(user);
    }

    private void validateApplicantData(ApplicantRegisterDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new InvalidRegisterException("name", "Имя обязательно");
        }
        if (!dto.getName().matches("^[a-zA-Zа-яА-Я\\s-]+$")) {
            throw new InvalidRegisterException("name", "Имя содержит недопустимые символы");
        }
        if (dto.getSurname() == null || dto.getSurname().trim().isEmpty()) {
            throw new InvalidRegisterException("surname", "Фамилия обязательна");
        }
        if (!dto.getSurname().matches("^[a-zA-Zа-яА-Я\\s-]+$")) {
            throw new InvalidRegisterException("surname", "Фамилия содержит недопустимые символы");
        }
        if (dto.getAge() == null || dto.getAge() < 18 || dto.getAge() > 50) {
            throw new InvalidRegisterException("age", "Возраст должен быть от 18 до 50 лет");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidRegisterException("email", "Неверный формат email");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new InvalidRegisterException("password", "Пароль должен содержать минимум 6 символов");
        }
        if (dto.getPhoneNumber() == null || !dto.getPhoneNumber().matches("^\\+?[0-9\\s-]{10,15}$")) {
            throw new InvalidRegisterException("phoneNumber", "Некорректный номер телефона");
        }
    }

    private void validateEmployerData(EmployerRegisterDTO dto) {
        if (dto.getCompanyName() == null || dto.getCompanyName().trim().isEmpty()) {
            throw new InvalidRegisterException("companyName", "Название компании обязательно");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidRegisterException("email", "Неверный формат email");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new InvalidRegisterException("password", "Пароль должен содержать минимум 6 символов");
        }
        if (dto.getPhoneNumber() == null || !dto.getPhoneNumber().matches("^\\+?[0-9\\s-]{10,15}$")) {
            throw new InvalidRegisterException("phoneNumber", "Некорректный номер телефона");
        }
    }

    @Override
    public List<UserDTO> getApplicantsVacancy(Long vacancyId) {
        log.info("Получение соискателей для вакансии ID: {}", vacancyId);
        if (vacancyId == null) {
            log.error("ID вакансии не может быть null");
            throw new InvalidUserDataException("ID вакансии не может быть null");
        }
        List<User> applicants = userDao.findApplicantsByVacancyId(vacancyId);
        if (applicants.isEmpty()) {
            log.warn("Не найдено соискателей для вакансии ID: {}", vacancyId);
            throw new UserNotFoundException("Не найдено соискателей для вакансии ID: " + vacancyId);
        }
        log.debug("Найдено {} соискателей для вакансии ID: {}", applicants.size(), vacancyId);
        return applicants.stream().map(this::convertToUserDTO).toList();
    }

    @Override
    public boolean userExists(String email) {
        log.debug("Проверка существования пользователя с email: {}", email);
        boolean exists = userDao.existsByEmail(email);
        log.trace("Результат проверки пользователя {}: {}", email, exists);
        return exists;
    }

    @Override
    public ResponseEntity<?> getAvatarByUserId(Long userId) {
        String avatarPath = userDao.findAvatarPathById(userId)
                .orElse(DEFAULT_AVATAR);

        return FileUtil.getOutputFile(avatarPath, MediaType.IMAGE_JPEG);
    }
}
