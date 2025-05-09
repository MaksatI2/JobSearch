package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.EditDTO.EditUserDTO;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.service.EditUserService;
import org.example.JobSearch.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditUserServiceImpl implements EditUserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateUserByEmail(String email, EditUserDTO editUserDTO) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));

        if (!existingUser.getPhoneNumber().equals(editUserDTO.getPhoneNumber())
                && userRepository.existsByPhoneNumber(editUserDTO.getPhoneNumber())) {
            throw new InvalidUserDataException("{user.phone.already.used}");
        }

        existingUser.setName(editUserDTO.getName());
        existingUser.setSurname(editUserDTO.getSurname());
        existingUser.setAge(editUserDTO.getAge());
        existingUser.setPhoneNumber(editUserDTO.getPhoneNumber());

        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void updateUserAvatar(Long userId, MultipartFile file) {
        log.info("Обновление аватара для пользователя ID: {}", userId);
        if (file == null || file.isEmpty()) {
            log.error("Файл для обновления аватара не может быть пустым");
            throw new InvalidUserDataException("Файл не может быть пустым");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь с ID не найден: " + userId);
                });

        try {
            String avatarPath = FileUtil.saveUploadFile(file, FileUtil.IMAGES_SUBDIR);
            log.debug("Аватар сохранен по пути: {}", avatarPath);

            user.setAvatar(avatarPath);
            userRepository.save(user);

            log.info("Аватар пользователя ID {} успешно обновлен", userId);
        } catch (Exception e) {
            log.error("Ошибка при обновлении аватара: {}", e.getMessage());
            throw new InvalidUserDataException("Ошибка при обновлении аватара: " + e.getMessage());
        }
    }
}
