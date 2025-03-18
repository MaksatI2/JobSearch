package org.example.JobSearch.service.impl;

import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    @Override
    public void createResume(ResumeDTO resumeDto) {
        //TODO: Сделать логику для создания резюме
    }

    @Override
    public void updateResume(Long resumeId, ResumeDTO resumeDto) {
        //TODO: Сделать логику для редактирования резюме
    }

    @Override
    public void deleteResume(Long resumeId) {
        //TODO: Сделать логику для удаления резюме
    }

    @Override
    public List<ResumeDTO> getAllResumes() {
        //TODO: Сделать логику для вывода всех резюме
        return List.of();
    }

    @Override
    public List<ResumeDTO> getResumesByCategory(Long categoryId) {
        //TODO: Сделать логику для вывода всех резюме по категории
        return List.of();
    }
}
