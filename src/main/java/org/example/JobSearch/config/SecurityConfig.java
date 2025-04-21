package org.example.JobSearch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String fetchUser = "SELECT email, password, enabled " +
                "FROM users " +
                "WHERE email = ?";

        String fetchRoles = """
                SELECT u.email, a.authority 
                FROM users u 
                JOIN authorities a ON u.account_type = a.id
                WHERE u.email = ?
                """;

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(fetchUser)
                .authoritiesByUsernameQuery(fetchRoles)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
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
                        .requestMatchers("/resumes/*/info").hasAnyAuthority("EMPLOYER", "APPLICANT")
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
                        .defaultSuccessUrl("/profile", true)
                        .failureUrl("/auth/login?error=true")
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