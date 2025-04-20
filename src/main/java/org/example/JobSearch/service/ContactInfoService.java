package org.example.JobSearch.service;

import org.example.JobSearch.dto.ContactInfoDTO;
import org.example.JobSearch.model.Resume;

import java.util.List;

public interface ContactInfoService {
    void createContactInfo(Long resumeId, ContactInfoDTO contactDto);

    void deleteByResumeId(Long resumeId);

    List<ContactInfoDTO> getContactInfoByResumeId(Long resumeId);
}