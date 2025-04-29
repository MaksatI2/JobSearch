package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.*;
import org.example.JobSearch.repository.FavoriteVacancyRepository;
import org.example.JobSearch.service.FavoriteVacancyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteVacancyServiceImpl implements FavoriteVacancyService {
    private final FavoriteVacancyRepository vacancyRepository;

    @Override
    public void addVacancyToFavorites(Long userId, Long vacancyId) {
        if (vacancyRepository.findByUserIdAndVacancyId(userId, vacancyId).isEmpty()) {
            FavoriteVacancy fav = FavoriteVacancy.builder()
                    .user(User.builder().id(userId).build())
                    .vacancy(Vacancy.builder().id(vacancyId).build())
                    .build();
            vacancyRepository.save(fav);
        }
    }

    @Override
    public Page<VacancyDTO> getUserFavoriteVacancies(Long userId, Pageable pageable) {
        long totalFavorites = vacancyRepository.countByUserId(userId);
        List<FavoriteVacancy> pagedFavorites = vacancyRepository.findByUserId(userId, pageable);

        List<VacancyDTO> vacancyDTOs = pagedFavorites.stream()
                .map(favorite -> convertToVacancyDTO(favorite.getVacancy()))
                .collect(Collectors.toList());

        return new PageImpl<>(vacancyDTOs, pageable, totalFavorites);
    }

    @Transactional
    @Override
    public void removeVacancyFromFavorites(Long userId, Long vacancyId) {
        vacancyRepository.deleteByUserIdAndVacancyId(userId, vacancyId);
    }

    @Override
    public List<Long> getFavoriteVacancyIds(Long userId) {
        return vacancyRepository.findVacancyIdsByUserId(userId);
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
}