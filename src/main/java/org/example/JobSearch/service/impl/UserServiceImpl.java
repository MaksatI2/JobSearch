package org.example.JobSearch.service.impl;

import org.example.JobSearch.util.FileUtil;
import org.example.JobSearch.dto.UserDTO;
import org.example.JobSearch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public void findApplicant(Long userEmail) {
        // TODO: сделать логику для поиска соискателя по email
    }

    @Override
    public void findEmployer(Long userEmail) {
        // TODO: сделать логику для поиска работодателя по email
    }

    @Override
    public void register(UserDTO userDto) {
        // TODO: сделать логику для регистрацию пользователя
    }

    @Override
    public void login(UserDTO userDto) {
        // TODO: сделать логику для авторизации пользователя
    }

    @Override
    public String addAvatar(MultipartFile file) {
        FileUtil fu = new FileUtil();
        return fu.saveUploadFile(file, "images/");
    }

    @Override
    public ResponseEntity<?> getAvatarByName(String imageName) {
        return new FileUtil().getOutputFile(imageName, "images/", MediaType.IMAGE_JPEG);
    }

    @Override
    public void getApplicantsVacancy(Long vacancyId) {
        //TODO: Сделать логику для вывода соискателей откликнувщихся на вакансию
    }
}
