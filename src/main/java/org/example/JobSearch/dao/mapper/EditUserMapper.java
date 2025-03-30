package org.example.JobSearch.dao.mapper;

import org.example.JobSearch.dto.EditDTO.EditUserDTO;
import org.example.JobSearch.model.User;
import org.springframework.stereotype.Component;

@Component
public class EditUserMapper {
    public User toUser(EditUserDTO editUserDTO) {
        User user = new User();
        user.setName(editUserDTO.getName());
        user.setSurname(editUserDTO.getSurname());
        user.setAge(editUserDTO.getAge());
        user.setPhoneNumber(editUserDTO.getPhoneNumber());
        return user;
    }
}