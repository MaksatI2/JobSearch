package org.example.JobSearch.service;

import org.example.JobSearch.dto.ContactInfoDTO;
import org.example.JobSearch.model.Resume;

import java.util.List;

public interface ContactInfoService {
    void createContactInfo(Long resumeId, ContactInfoDTO contactDto);

    List<ContactInfoDTO> getContactInfoByResumeId(Long resumeId);
}