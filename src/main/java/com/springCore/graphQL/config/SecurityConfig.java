package com.springCore.graphQL.config;

import com.springCore.graphQL.entity.User; // <-- Import your User entity
import com.springCore.graphQL.repository.UserRepository; // <-- Import your UserRepository
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/graphql/**", "/graphiql/**").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // This method replaces the need for a separate UserDetailsServiceImpl class
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = (User) userRepository.findByUserName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    Stream.of(user.getRole())
                            // The role name from DB is "ADMIN"
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList())
            );
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow requests from the default React dev server port (3000)
                registry.addMapping("/graphql/**") // Only configure it for the /graphql endpoint
                        .allowedOriginPatterns("*")
                        .allowedMethods("POST", "GET")
                        .allowedHeaders("*");
//                registry.addMapping("/subscribeToServerEvents") // Only configure it for the /graphql endpoint
//                        .allowedOriginPatterns("*")
//                        .allowedMethods("POST", "GET")
//                        .allowedHeaders("*");

            }
        };
    }
}