package com.EDTECH.QUIZ.GAME.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2UserService;
import com.EDTECH.QUIZ.GAME.sevices.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, 
                          CustomLoginSuccessHandler customLoginSuccessHandler, 
                          CustomUserDetailsService customUserDetailsService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security filter chain configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/register", "/login", "/styles/**", "/images/**", "/icons/**").permitAll()  // Public access
                .anyRequest().authenticated()  
            )
            .formLogin(form -> form
                .loginPage("/login")  
                .usernameParameter("username")  
                .passwordParameter("password")
                .successHandler(customLoginSuccessHandler)  
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                    
                )
                .successHandler((request, response, authentication) -> {
                    CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
                    System.out.println("OAuth2 User: " + customUser);
                    response.sendRedirect("/home");
                })
            )
            .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        );


        return http.build();
    }
}