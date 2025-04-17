package org.example.JobSearch.repository;

import org.example.JobSearch.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}