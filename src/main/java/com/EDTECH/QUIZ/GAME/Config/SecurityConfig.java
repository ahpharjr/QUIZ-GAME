package com.EDTECH.QUIZ.GAME.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2UserService;
import com.EDTECH.QUIZ.GAME.sevices.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomLoginOauthSuccessHandler customLoginOauthSuccessHandler;

    public SecurityConfig(CustomLoginOauthSuccessHandler customLoginOauthSuccessHandler, 
                          CustomLoginSuccessHandler customLoginSuccessHandler, 
                          CustomUserDetailsService customUserDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomOAuth2UserService customOAuth2UserService) {
        this.customLoginOauthSuccessHandler = customLoginOauthSuccessHandler;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/register", "/login", "/verify-email", "/styles/**", "/images/**", "/icons/**", "/oauth2/**").permitAll()
            .anyRequest().authenticated()
        )

        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless before adding filter
        .formLogin(form -> form
            .loginPage("/login")
            // .usernameParameter("username")
            // .passwordParameter("password")
            .successHandler(customLoginSuccessHandler)
            .permitAll()
        )
        .oauth2Login( oauth2 -> oauth2
            .loginPage("/login")
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
            .successHandler(customLoginOauthSuccessHandler)
        )
        .userDetailsService(customUserDetailsService) 
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessHandler(new CustomLogoutHandler())
            .permitAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


}