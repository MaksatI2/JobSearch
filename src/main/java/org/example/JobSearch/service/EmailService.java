package org.example.JobSearch.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendEmail(String email, String resetPasswordLink)
            throws UnsupportedEncodingException, MessagingException;
}
