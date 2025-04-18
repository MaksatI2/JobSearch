package org.example.JobSearch.repository;

import org.example.JobSearch.model.AccountType;
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

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.accountType = :type")
    Optional<User> findEmployerByEmail(@Param("email") String email, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.accountType = :type")
    Optional<User> findEmployerByPhone(@Param("phoneNumber") String phoneNumber, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.name = :name AND u.accountType = :type")
    List<User> findEmployersByName(@Param("name") String name, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.accountType = :type")
    Optional<User> findApplicantByEmail(@Param("email") String email, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.accountType = :type")
    Optional<User> findApplicantByPhone(@Param("phoneNumber") String phoneNumber, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.name = :name AND u.accountType = :type")
    List<User> findApplicantsByName(@Param("name") String name, @Param("type") AccountType type);

    @Query("SELECT u FROM User u JOIN u.resumes r JOIN r.respondedApplicants v WHERE v.id = :vacancyId")
    List<User> findApplicantsByVacancyId(@Param("vacancyId") Long vacancyId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u WHERE u.id = :userId AND u.accountType = :type")
    boolean isUserOfType(@Param("userId") Long userId, @Param("type") AccountType type);

    @Query("SELECT u.avatar FROM User u WHERE u.id = :userId")
    Optional<String> findAvatarPathById(@Param("userId") Long userId);
}