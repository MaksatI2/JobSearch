package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dao.EditUserDao;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dao.mapper.EditUserMapper;
import org.example.JobSearch.dto.EditDTO.EditUserDTO;
import org.example.JobSearch.exceptions.InvalidUserDataException;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.User;
import org.example.JobSearch.service.EditUserService;
import org.example.JobSearch.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EditUserServiceImpl implements EditUserService {
    private final EditUserDao editUserDao;
    private final EditUserMapper editUserMapper;
    private final UserDao userDao;

    @Override
    @Transactional
    public void updateUserByEmail(String email, EditUserDTO editUserDTO) {
        User existingUser = userDao.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + email));

        User userToUpdate = editUserMapper.toUser(editUserDTO);
        userToUpdate.setPassword(existingUser.getPassword());
        userToUpdate.setAccountType(existingUser.getAccountType());
        userToUpdate.setAvatar(existingUser.getAvatar());

        int updatedRows = editUserDao.updateUserByEmail(email, userToUpdate);
        if (updatedRows == 0) {
            throw new InvalidUserDataException("Ошибка обновления: " + email);
        }
    }

    @Override
    public void updateUserAvatar(Long userId, MultipartFile file) {
        log.info("Обновление аватара для пользователя ID: {}", userId);
        if (file == null || file.isEmpty()) {
            log.error("Файл для обновления аватара не может быть пустым");
            throw new InvalidUserDataException("Файл не может быть пустым");
        }

        editUserDao.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь с ID не найден: " + userId);
                });

        try {
            String avatarPath = FileUtil.saveUploadFile(file, FileUtil.IMAGES_SUBDIR);
            log.debug("Аватар сохранен по пути: {}", avatarPath);

            int updatedRows = editUserDao.updateUserAvatar(userId, avatarPath);

            if (updatedRows == 0) {
                log.error("Не удалось обновить аватар для пользователя ID: {}", userId);
                throw new InvalidUserDataException("Не удалось обновить аватар для пользователя ID: " + userId);
            }
            log.info("Аватар пользователя ID {} успешно обновлен", userId);
        } catch (Exception e) {
            log.error("Ошибка при обновлении аватара: {}", e.getMessage());
            throw new InvalidUserDataException("Ошибка при обновлении аватара: " + e.getMessage());
        }
    }
}