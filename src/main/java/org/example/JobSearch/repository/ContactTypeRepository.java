package org.example.JobSearch.repository;

import org.example.JobSearch.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
}