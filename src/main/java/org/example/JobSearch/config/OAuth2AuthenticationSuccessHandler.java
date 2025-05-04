package org.example.JobSearch.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.model.User;
import org.example.JobSearch.repository.UserRepository;
import org.example.JobSearch.service.oauth2.GoogleUserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        GoogleUserInfo googleUserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        String email = googleUserInfo.getEmail();

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            setDefaultTargetUrl("/");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("oauth2UserInfo", googleUserInfo);
            setDefaultTargetUrl("/auth/oauth2/account-type");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}