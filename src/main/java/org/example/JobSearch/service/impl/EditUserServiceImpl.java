package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dao.EditUserDao;
import org.example.JobSearch.dao.UserDao;
import org.example.JobSearch.dao.mapper.EditUserMapper;
import org.example.JobSearch.dto.EditUserDTO;
import org.example.JobSearch.exceptions.UserNotFoundException;
import org.example.JobSearch.model.User;
import org.example.JobSearch.service.EditUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        User userToUpdate = editUserMapper.toUser(editUserDTO);
        userToUpdate.setEmail(email);
        userToUpdate.setPassword(existingUser.getPassword());
        userToUpdate.setAccountType(existingUser.getAccountType());
        userToUpdate.setAvatar(existingUser.getAvatar());

        int updatedRows = editUserDao.updateUserByEmail(email, userToUpdate);

        if (updatedRows == 0) {
            throw new RuntimeException("Failed to update user with email: " + email);
        }
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        if (!userDao.existsByEmail(email)) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        int deletedRows = editUserDao.deleteUserByEmail(email);
        if (deletedRows == 0) {
            throw new RuntimeException("Failed to delete user with email: " + email);
        }
    }
}