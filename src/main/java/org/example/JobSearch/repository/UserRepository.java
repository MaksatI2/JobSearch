package org.example.JobSearch.repository;

import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetPasswordToken(String token);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.accountType = :type")
    Optional<User> findByEmailAndType(@Param("email") String email, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.accountType = :type")
    Optional<User> findByPhoneAndType(@Param("phoneNumber") String phoneNumber, @Param("type") AccountType type);

    @Query("SELECT u FROM User u WHERE u.name = :name AND u.accountType = :type")
    List<User> findByNameAndType(@Param("name") String name, @Param("type") AccountType type);

    @Query("SELECT u FROM User u JOIN u.resumes r JOIN r.respondedApplicants v WHERE v.id = :vacancyId")
    List<User> findApplicantsByVacancyId(@Param("vacancyId") Long vacancyId);

    @Query("SELECT u FROM User u WHERE u.accountType = :type")
    Page<User> findAllByAccountType(@Param("type") AccountType type, Pageable pageable);


}