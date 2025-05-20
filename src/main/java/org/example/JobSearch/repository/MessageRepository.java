package org.example.JobSearch.repository;

import org.example.JobSearch.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRespondedApplicantIdOrderBySendTimeAsc(Long respondedApplicantId);

    @Query(value = "SELECT * FROM messages m WHERE m.responded_applicants = :respondedApplicantId ORDER BY m.send_time DESC LIMIT :limit",
            nativeQuery = true)
    List<Message> findByRespondedApplicantIdOrderBySendTimeDesc(@Param("respondedApplicantId") Long respondedApplicantId,
                                                                @Param("limit") int limit);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.respondedApplicant.id = :respondedApplicantId AND m.senderType = 'APPLICANT'")
    void markMessagesAsReadForEmployer(@Param("respondedApplicantId") Long respondedApplicantId);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.respondedApplicant.id = :respondedApplicantId AND m.senderType = 'EMPLOYER'")
    void markMessagesAsReadForApplicant(@Param("respondedApplicantId") Long respondedApplicantId);

    @Query("SELECT COUNT(m) FROM Message m " +
           "JOIN m.respondedApplicant ra " +
           "JOIN ra.vacancy v " +
           "JOIN v.author e " +
           "WHERE e.email = :email AND m.senderType = 'APPLICANT' AND m.isRead = false")
    int countUnreadMessagesForEmployer(@Param("email") String email);

    @Query("SELECT COUNT(m) FROM Message m " +
           "JOIN m.respondedApplicant ra " +
           "JOIN ra.resume r " +
           "JOIN r.applicant a " +
           "WHERE a.email = :email AND m.senderType = 'EMPLOYER' AND m.isRead = false")
    int countUnreadMessagesForApplicant(@Param("email") String email);

    @Query("SELECT COUNT(m) FROM Message m " +
           "JOIN m.respondedApplicant ra " +
           "WHERE ra.id = :respondedApplicantId AND m.isRead = false " +
           "AND m.senderType = CASE WHEN :isEmployer = true THEN 'APPLICANT' ELSE 'EMPLOYER' END")
    int countUnreadMessagesInConversation(@Param("respondedApplicantId") Long respondedApplicantId,
                                          @Param("isEmployer") boolean isEmployer);
}