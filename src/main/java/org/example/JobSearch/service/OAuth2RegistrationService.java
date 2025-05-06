package org.example.JobSearch.service;

import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.service.oauth2.GoogleUserInfo;

public interface OAuth2RegistrationService {
    void saveUserFromOAuth(GoogleUserInfo userInfo, AccountType accountType, String phoneNumber, Integer age, String surname);
}
