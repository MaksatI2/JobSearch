package org.example.JobSearch.service.oauth2;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.exceptions.InvalidRegisterException;
import org.example.JobSearch.model.AccountType;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.service.OAuth2RegistrationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OAuth2RegistrationServiceImpl implements OAuth2RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");
    private static final Pattern SURNAME_PATTERN = Pattern.compile("^[A-Za-zА-Яа-яЁё\\s-]+$");

    @Override
    public void saveUserFromOAuth(GoogleUserInfo userInfo, AccountType accountType,
                                  String phoneNumber, Integer age, String surname) {
        validateInputs(accountType, phoneNumber, age, surname);

        User newUser = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .surname(accountType == AccountType.APPLICANT ? (surname != null ? surname : "") : "")
                .age(accountType == AccountType.APPLICANT ? age : null)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .accountType(accountType)
                .enabled(true)
                .phoneNumber(phoneNumber)
                .avatar(userInfo.getPicture())
                .build();

        userRepository.save(newUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                newUser.getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(accountType.getName()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void validateInputs(AccountType accountType, String phoneNumber, Integer age, String surname) {
        if (phoneNumber == null || !PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidRegisterException("phoneNumber", "Номер телефона должен быть в формате +996700123456");
        }

        if (accountType == AccountType.APPLICANT) {
            if (age == null || age < 18 || age > 60) {
                throw new InvalidRegisterException("age", "Возраст должен быть от 18 до 60 лет");
            }

            if (surname == null || surname.isEmpty() || !SURNAME_PATTERN.matcher(surname).matches()) {
                throw new InvalidRegisterException("surname", "Фамилия должна содержать только буквы и дефисы");
            }
            if (userRepository.existsByPhoneNumber(phoneNumber)) {
                throw new InvalidRegisterException("phoneNumber", "Этот номер телефона уже зарегистрирован");
            }
        }
    }
}