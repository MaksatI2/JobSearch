package org.example.JobSearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.JobSearch.dto.ContactInfoDTO;
import org.example.JobSearch.model.ContactInfo;
import org.example.JobSearch.model.ContactType;
import org.example.JobSearch.model.Resume;
import org.example.JobSearch.repository.ContactInfoRepository;
import org.example.JobSearch.repository.ContactTypeRepository;
import org.example.JobSearch.repository.ResumeRepository;
import org.example.JobSearch.service.ContactInfoService;
import org.example.JobSearch.service.ContactTypeService;
import org.example.JobSearch.service.ResumeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;
    private final ResumeService resumeService;
    private final ContactTypeService contactTypeService;

    @Override
    @Transactional
    public void createContactInfo(Long resumeId, ContactInfoDTO contactDto) {
        Resume resume = resumeService.getResumeEntityById(resumeId);
        ContactType type = contactTypeService.getById(contactDto.getTypeId());

        ContactInfo contact = new ContactInfo();
        contact.setResume(resume);
        contact.setType(type);
        contact.setValue(contactDto.getValue());

        contactInfoRepository.save(contact);
        log.debug("Создан контакт типа {} для резюме ID: {}", type.getName(), resumeId);
    }

    @Override
    @Transactional
    public void deleteByResumeId(Long resumeId) {
        contactInfoRepository.deleteByResumeId(resumeId);
    }

    @Override
    public List<ContactInfoDTO> getContactInfoByResumeId(Long resumeId) {
        return contactInfoRepository.findByResumeId(resumeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ContactInfoDTO convertToDTO(ContactInfo contact) {
        ContactInfoDTO dto = new ContactInfoDTO();
        dto.setId(contact.getId());
        dto.setTypeId(contact.getType().getId());
        dto.setTypeName(contact.getType().getName());
        dto.setValue(contact.getValue());
        return dto;
    }
}
