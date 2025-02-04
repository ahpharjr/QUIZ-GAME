package com.EDTECH.QUIZ.GAME.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2User;
import com.EDTECH.QUIZ.GAME.sevices.CustomOAuth2UserService;
import com.EDTECH.QUIZ.GAME.sevices.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, 
                          CustomLoginSuccessHandler customLoginSuccessHandler, 
                          CustomUserDetailsService customUserDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security filter chain configuration
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())  
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/register", "/login", "/styles/**", "/images/**", "/icons/**").permitAll()  // Public access
//                 .anyRequest().authenticated()  
//             )
//             .formLogin(form -> form
//                 .loginPage("/login")  
//                 .usernameParameter("username")  
//                 .passwordParameter("password")
//                 .successHandler(customLoginSuccessHandler)  
//                 .permitAll()
//             )
//             .oauth2Login(oauth2 -> oauth2
//                 .loginPage("/login")
//                 .userInfoEndpoint(userInfo -> userInfo
//                     .userService(customOAuth2UserService)
//                 )
//                 .successHandler((request, response, authentication) -> {
//                     CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
//                     System.out.println("OAuth2 User: " + customUser);
//                     request.getSession().setAttribute("user", customUser); 
//                     response.sendRedirect("/home");
//                 })
//             )
//             .logout(logout -> logout
//             .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//             .logoutSuccessUrl("/login?logout")
//             .invalidateHttpSession(true)
//             .deleteCookies("JSESSIONID")
//             .permitAll()
//         );


//         return http.build();
//     }

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/register", "/login", "/styles/**", "/images/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless before adding filter
        .formLogin(form -> form
            .loginPage("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(customLoginSuccessHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessHandler(new CustomLogoutHandler())
            .permitAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    // Authentication manager bean
    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }

    // // Authentication provider bean
    // @Bean
    // public AuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    //     provider.setUserDetailsService(customUserDetailsService);
    //     provider.setPasswordEncoder(passwordEncoder());
    //     return provider;
    // }

    // // In-memory user details manager bean
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails user = User.withUsername("user")
    //         .password(passwordEncoder().encode("password"))
    //         .roles("USER")
    //         .build();
    //     return new InMemoryUserDetailsManager(user);
    // }


}