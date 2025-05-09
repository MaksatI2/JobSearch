package org.example.JobSearch.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.JobSearch.repository.UserRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserLocaleFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final CookieLocaleResolver localeResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestLang = request.getParameter("lang");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails userDetails) {
            userRepository.findByEmail(userDetails.getUsername()).ifPresent(user -> {
                Locale userLocale = new Locale(user.getLanguage());

                if (requestLang != null && (requestLang.equals("ru") || requestLang.equals("en"))) {
                    if (!requestLang.equals(user.getLanguage())) {
                        user.setLanguage(requestLang);
                        userRepository.save(user);
                    }
                    LocaleContextHolder.setLocale(new Locale(requestLang));
                    localeResolver.setLocale(request, response, new Locale(requestLang));
                }
                else {
                    LocaleContextHolder.setLocale(userLocale);
                    localeResolver.setLocale(request, response, userLocale);
                }
            });
        }
        else {
            if (requestLang != null && (requestLang.equals("ru") || requestLang.equals("en"))) {
                LocaleContextHolder.setLocale(new Locale(requestLang));
            }
        }

        filterChain.doFilter(request, response);
    }
}