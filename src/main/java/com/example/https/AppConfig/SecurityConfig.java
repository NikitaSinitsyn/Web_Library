package com.example.https.AppConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    // Создаем экземпляр PasswordEncoder для шифрования паролей.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(this::customizeRequest);
        return http.build();

    }
    private void customizeRequest(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        try {
            registry.requestMatchers(new AntPathRequestMatcher("/admin/**"))
                    .hasAnyRole("ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/**"))
                    .hasAnyRole("USER")
                    .and()
                    .formLogin().permitAll()
                    .and()
                    .logout().logoutUrl("/logout");
            // Настраиваем механизм выхода из системы
            // с заданием URL "/logout".

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}