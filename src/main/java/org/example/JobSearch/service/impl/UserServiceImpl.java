package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dto.UserDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        log.info("Регистрация нового пользователя: {}", userDto.getEmail());
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setPhoneNumber(userDto.getPhoneNumber());
        if (userDto.getAccountType() == AccountType.APPLICANT) {
            user.setAvatar(FileUtil.DEFAULT_APPLICANT_AVATAR);
        } else {
            user.setAvatar(FileUtil.DEFAULT_EMPLOYER_AVATAR);
        }
        user.setAccountType(userDto.getAccountType());

        validateUser(userDto);

        userDao.save(user);
        log.info("Пользователь {} успешно зарегистрирован", userDto.getEmail());
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
    public void updateUserAvatar(Long userId, MultipartFile file) {
        log.info("Обновление аватара для пользователя ID: {}", userId);
        if (file == null || file.isEmpty()) {
            log.error("Файл для обновления аватара не может быть пустым");
            throw new InvalidUserDataException("Файл не может быть пустым");
        }

        userDao.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь с ID не найден: " + userId);
                });

        try {
            String avatarPath = FileUtil.saveUploadFile(file, "images/");
            log.debug("Аватар сохранен по пути: {}", avatarPath);

            int updatedRows = userDao.updateUserAvatar(userId, avatarPath);

            if (updatedRows == 0) {
                log.error("Не удалось обновить аватар для пользователя ID: {}", userId);
                throw new RuntimeException("Не удалось обновить аватар для пользователя ID: " + userId);
            }
            log.info("Аватар пользователя ID {} успешно обновлен", userId);
        } catch (Exception e) {
            log.error("Ошибка при обновлении аватара: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении аватара: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAvatarByUserId(Long userId) {
        String avatarFilename = userDao.findAvatarPathById(userId)
                .orElseGet(() -> {
                    User user = userDao.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
                    return user.getAccountType() == AccountType.APPLICANT
                            ? FileUtil.DEFAULT_APPLICANT_AVATAR
                            : FileUtil.DEFAULT_EMPLOYER_AVATAR;
                });

        return FileUtil.getOutputFile(avatarFilename, FileUtil.IMAGES_SUBDIR, MediaType.IMAGE_JPEG);
    }

    private void validateUser(UserDTO userDto) {
        log.debug("Валидация данных пользователя: {}", userDto.getEmail());
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            log.error("Email не может быть пустым");
            throw new InvalidUserDataException("Email не может быть пустым");
        }
        if (userDao.existsByEmail(userDto.getEmail())) {
            log.error("Email {} уже используется", userDto.getEmail());
            throw new InvalidUserDataException("Email уже используется");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            log.error("Пароль не может быть пустым");
            throw new InvalidUserDataException("Пароль не может быть пустым");
        }
        if (userDto.getPassword().length() < 6) {
            log.error("Пароль должен содержать минимум 6 символов");
            throw new InvalidUserDataException("Пароль должен содержать минимум 6 символов");
        }
        if (userDto.getName() == null || userDto.getName().trim().isEmpty()) {
            log.error("Имя не может быть пустым");
            throw new InvalidUserDataException("Имя не может быть пустым");
        }
        if (userDto.getSurname() == null || userDto.getSurname().trim().isEmpty()) {
            log.error("Фамилия не может быть пустым");
            throw new InvalidUserDataException("Фамилия не может быть пустым");
        }
        if (userDto.getAge() == null) {
            log.error("Возраст не может быть null");
            throw new InvalidUserDataException("Возраст не может быть null");
        }
        if (userDto.getAge() < 18 || userDto.getAge() > 60) {
            log.error("Возраст должен быть между 18 и 60");
            throw new InvalidUserDataException("Возраст должен быть между 18 и 60");
        }
        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().matches("^\\d{10,15}$")) {
            log.error("Некорректный номер телефона: {}", userDto.getPhoneNumber());
            throw new InvalidUserDataException("Номер телефона должен содержать только цифры и быть длиной 10-15 символов");
        }
        if (userDto.getAccountType() == null) {
            log.error("Тип аккаунта не может быть null");
            throw new InvalidUserDataException("Тип аккаунта не может быть null");
        }
        if (!userDto.getName().matches("^[a-zA-Zа-яА-Я]+$")) {
            log.error("Имя содержит недопустимые символы: {}", userDto.getName());
            throw new InvalidUserDataException("Имя не может содержать цифры или специальные символы");
        }
        if (!userDto.getSurname().matches("^[a-zA-Zа-яА-Я]+$")) {
            log.error("Фамилия содержит недопустимые символы: {}", userDto.getSurname());
            throw new InvalidUserDataException("Фамилия не может содержать цифры или специальные символы");
        }
        log.debug("Валидация пользователя {} прошла успешно", userDto.getEmail());
    }

}
