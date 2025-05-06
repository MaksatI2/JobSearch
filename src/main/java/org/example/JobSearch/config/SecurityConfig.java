package org.example.JobSearch.config;

import lombok.RequiredArgsConstructor;
import org.example.JobSearch.service.impl.CustomOAuth2UserService;
import org.example.JobSearch.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomSuccessHandler successHandler;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/",
                                "/auth/**",
                                "/static/**",
                                "/favicon.ico",
                                "/error",
                                "/vacancies",
                                "/vacancies/*/info",
                                "/vacancies/category/**"
                        ).permitAll()
                        .requestMatchers("/profile/viewApplicant/*").hasAuthority("EMPLOYER")
                        .requestMatchers("/profile/viewEmployer/*").hasAuthority("APPLICANT")
                        .requestMatchers("/favorites/vacancies").hasAuthority("APPLICANT")
                        .requestMatchers("/favorites/resumes").hasAuthority("EMPLOYER")
                        .requestMatchers("/resumes/*/info").authenticated()
                        .requestMatchers("/vacancies/*/favorite").authenticated()
                        .requestMatchers("/vacancies/*/unFavorite").authenticated()
                        .requestMatchers("/resumes/allResumes").hasAuthority("EMPLOYER")
                        .requestMatchers("/resumes/**").hasAuthority("APPLICANT")
                        .requestMatchers("/users/applicants/**").hasAuthority("APPLICANT")
                        .requestMatchers("/vacancies/**").hasAuthority("EMPLOYER")
                        .requestMatchers("/users/employers/**").hasAuthority("EMPLOYER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/errors/403")
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        return http.build();
    }
}