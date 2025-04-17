package org.example.JobSearch.repository;

import org.example.JobSearch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u JOIN u.accountType a WHERE u.email = :email AND a.authority = 'EMPLOYER'")
    Optional<User> findEmployerByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN u.accountType a WHERE u.phoneNumber = :phoneNumber AND a.authority = 'EMPLOYER'")
    Optional<User> findEmployerByPhone(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM User u JOIN u.accountType a WHERE u.name = :name AND a.authority = 'EMPLOYER'")
    List<User> findEmployersByName(@Param("name") String name);

    @Query("SELECT u FROM User u JOIN u.accountType a WHERE u.email = :email AND a.authority = 'APPLICANT'")
    Optional<User> findApplicantByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN u.accountType a WHERE u.phoneNumber = :phoneNumber AND a.authority = 'APPLICANT'")
    Optional<User> findApplicantByPhone(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM User u JOIN u.accountType a WHERE u.name = :name AND a.authority = 'APPLICANT'")
    List<User> findApplicantsByName(@Param("name") String name);

    @Query("SELECT u FROM User u JOIN u.resumes r JOIN r.respondedVacancies v WHERE v.id = :vacancyId")
    List<User> findApplicantsByVacancyId(@Param("vacancyId") Long vacancyId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.accountType a WHERE u.id = :userId AND a.authority = 'EMPLOYER'")
    boolean isUserEmployer(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.accountType a WHERE u.id = :userId AND a.authority = 'APPLICANT'")
    boolean isUserApplicant(@Param("userId") Long userId);

    @Query("SELECT u.avatar FROM User u WHERE u.id = :userId")
    Optional<String> findAvatarPathById(@Param("userId") Long userId);
}