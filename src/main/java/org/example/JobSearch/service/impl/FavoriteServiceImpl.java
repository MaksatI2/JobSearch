package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.dto.VacancyDTO;
import org.example.JobSearch.model.FavoriteVacancy;
import org.example.JobSearch.repository.FavoriteVacancyRepository;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.repository.VacancyRepository;
import org.example.JobSearch.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteVacancyRepository repository;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    public void addToFavorites(Long userId, Long vacancyId) {
        if (repository.findByUserIdAndVacancyId(userId, vacancyId).isEmpty()) {
            FavoriteVacancy fav = FavoriteVacancy.builder()
                    .user(userRepository.getReferenceById(userId))
                    .vacancy(vacancyRepository.getReferenceById(vacancyId))
                    .build();
            repository.save(fav);
        }
    }

    @Override
    public List<VacancyDTO> getUserFavorites(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(favorite -> {
                    var vacancy = favorite.getVacancy();
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
                })
                .toList();
    }

    @Override
    @Transactional
    public void removeFromFavorites(Long userId, Long vacancyId) {
        repository.deleteByUserIdAndVacancyId(userId, vacancyId);
    }

    @Override
    public List<Long> getFavoriteVacancyIds(Long userId) {
        return repository.findVacancyIdsByUserId(userId);
    }
}
