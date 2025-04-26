package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.ResumeDTO;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.*;
import org.example.JobSearch.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteVacancyRepository vacancyRepository;
    private final FavoriteResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepo;
    private final ResumeRepository resumeRepo;

    @Override
    public void addVacancyToFavorites(Long userId, Long vacancyId) {
        if (vacancyRepository.findByUserIdAndVacancyId(userId, vacancyId).isEmpty()) {
            FavoriteVacancy fav = FavoriteVacancy.builder()
                    .user(userRepository.getReferenceById(userId))
                    .vacancy(vacancyRepo.getReferenceById(vacancyId))
                    .build();
            vacancyRepository.save(fav);
        }
    }

    @Override
    public List<VacancyDTO> getUserFavoriteVacancies(Long userId) {
        return vacancyRepository.findByUserId(userId).stream()
                .map(favorite -> convertToVacancyDTO(favorite.getVacancy()))
                .toList();
    }

    @Override
    @Transactional
    public void removeVacancyFromFavorites(Long userId, Long vacancyId) {
        vacancyRepository.deleteByUserIdAndVacancyId(userId, vacancyId);
    }

    @Override
    public List<Long> getFavoriteVacancyIds(Long userId) {
        return vacancyRepository.findVacancyIdsByUserId(userId);
    }

    @Override
    public void addResumeToFavorites(Long userId, Long resumeId) {
        if (resumeRepository.findByUserIdAndResumeId(userId, resumeId).isEmpty()) {
            FavoriteResume fav = FavoriteResume.builder()
                    .user(userRepository.getReferenceById(userId))
                    .resume(resumeRepo.getReferenceById(resumeId))
                    .build();
            resumeRepository.save(fav);
        }
    }

    @Override
    public List<ResumeDTO> getUserFavoriteResumes(Long userId) {
        return resumeRepository.findByUserId(userId).stream()
                .map(favorite -> convertToResumeDTO(favorite.getResume()))
                .toList();
    }

    @Override
    @Transactional
    public void removeResumeFromFavorites(Long userId, Long resumeId) {
        resumeRepository.deleteByUserIdAndResumeId(userId, resumeId);
    }

    @Override
    public List<Long> getFavoriteResumeIds(Long userId) {
        return resumeRepository.findResumeIdsByUserId(userId);
    }

    private VacancyDTO convertToVacancyDTO(Vacancy vacancy) {
        return VacancyDTO.builder()
                .id(vacancy.getId())
                .authorId(vacancy.getAuthor().getId())
                .authorName(vacancy.getAuthor().getName())
                .categoryId(vacancy.getCategory().getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .salary(vacancy.getSalary())
                .expFrom(vacancy.getExpFrom())
                .expTo(vacancy.getExpTo())
                .isActive(vacancy.getIsActive())
                .updateTime(vacancy.getUpdateTime())
                .build();
    }

    private ResumeDTO convertToResumeDTO(Resume resume) {
        return ResumeDTO.builder()
                .id(resume.getId())
                .applicantId(resume.getApplicant().getId())
                .applicantName(resume.getApplicant().getName())
                .applicantAge(resume.getApplicant().getAge())
                .applicantAvatar(resume.getApplicant().getAvatar())
                .categoryId(resume.getCategory().getId())
                .name(resume.getName())
                .salary(resume.getSalary())
                .isActive(resume.getIsActive())
                .createDate(resume.getCreateDate())
                .updateTime(resume.getUpdateTime())
                .build();
    }
}