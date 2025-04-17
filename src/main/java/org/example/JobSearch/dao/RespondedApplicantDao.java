package org.example.JobSearch.dao;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.RespondedApplicant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RespondedApplicantDao {

    private final JdbcTemplate jdbcTemplate;

    public void save(RespondedApplicant applicant) {
        String sql = "INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, applicant.getResumeId(), applicant.getVacancyId(), applicant.getConfirmation());
    }

    public boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId) {
        String sql = "SELECT COUNT(*) FROM responded_applicants WHERE resume_id = ? AND vacancy_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, resumeId, vacancyId);
        return count != null && count > 0;
    }

    public int countByApplicantId(Long applicantId) {
        String sql = """
            SELECT COUNT(*) 
            FROM responded_applicants ra
            JOIN resumes r ON ra.resume_id = r.id
            WHERE r.applicant_id = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, applicantId);
    }
}
