package com.EDTECH.QUIZ.GAME.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.EDTECH.QUIZ.GAME.sevices.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if( requestURI.startsWith("/styles/") || requestURI.startsWith("/images/") || 
            requestURI.startsWith("/register") || requestURI.startsWith("/login") || 
            requestURI.startsWith("/oauth2/") || requestURI.startsWith("/forgot-password")||
            requestURI.startsWith("/verify-otp") || requestURI.startsWith("/reset-password/")||
            requestURI.startsWith("/email-verification") || requestURI.startsWith("/resend-verification")){
            filterChain.doFilter(request, response);
            return ;
        }


        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
            System.out.println("This is the token value Check in the cookies\n Token ****" + token + "*****");

        }else {
            System.out.println("Cookies are null in JwtAuthenticationFilter");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token is missing");
            return;
        }

        if (token != null) {
            String username = jwtService.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }else {
                System.out.println("Username is null in JwtAuthenticationFilter");
            }
        }else {
            System.out.println("Token is null in JwtAuthenticationFilter");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        return path.equals("/") ||         // Allow Landing Page
            path.startsWith("/register") ||
            path.startsWith("/login") ||
            path.startsWith("/verify-email") ||
            path.startsWith("/styles/") ||
            path.startsWith("/images/") ||
            path.startsWith("/icons/") ||
            path.startsWith("/oauth2/") ||
            path.startsWith("/reset-password") ||
            path.startsWith("/verify-token") || 
            path.startsWith("/forgot-password");  // Allow OAuth login
    }

}


// @Component
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final JwtService jwtService;
//     private final UserDetailsService userDetailsService;

//     @Autowired
//     public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
//         this.jwtService = jwtService;
//         this.userDetailsService = userDetailsService;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {

//         if (shouldNotFilter(request)) {
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String token = extractTokenFromRequest(request);

//         if (token == null) {
//             System.out.println("❌ Token is null in JwtAuthenticationFilter");
//             filterChain.doFilter(request, response);
//             return;
//         }

//         String username = jwtService.extractUsername(token);

//         if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//             UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//             if (jwtService.isTokenValid(token, userDetails)) {
//                 UsernamePasswordAuthenticationToken authentication =
//                         new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//                 System.out.println("✅ JWT Authentication successful for user: " + username);
//             } else {
//                 System.out.println("❌ JWT is invalid or expired");
//             }
//         }

//         filterChain.doFilter(request, response);
//     }

//     private String extractTokenFromRequest(HttpServletRequest request) {
//         // 1. Try Authorization header
//         String authHeader = request.getHeader("Authorization");
//         if (authHeader != null && authHeader.startsWith("Bearer ")) {
//             return authHeader.substring(7);
//         }

//         // 2. Try cookies
//         if (request.getCookies() != null) {
//             for (Cookie cookie : request.getCookies()) {
//                 if ("JWT_TOKEN".equals(cookie.getName())) {
//                     return cookie.getValue();
//                 }
//             }
//         }

//         return null;
//     }

//     @Override
//     protected boolean shouldNotFilter(HttpServletRequest request) {
//         String path = request.getRequestURI();

//         return path.equals("/") ||
//                 path.startsWith("/register") ||
//                 path.startsWith("/login") ||
//                 path.startsWith("/verify-email") ||
//                 path.startsWith("/styles/") ||
//                 path.startsWith("/images/") ||
//                 path.startsWith("/icons/") ||
//                 path.startsWith("/oauth2/") ||
//                 path.startsWith("/reset-password") ||
//                 path.startsWith("/verify-token") ||
//                 path.startsWith("/forgot-password");
//     }
// }
