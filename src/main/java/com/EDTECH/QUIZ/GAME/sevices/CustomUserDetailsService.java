package com.EDTECH.QUIZ.GAME.sevices;

import java.util.Collections;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.EDTECH.QUIZ.GAME.models.Users;
import com.EDTECH.QUIZ.GAME.models.Users.AuthProvider;
import com.EDTECH.QUIZ.GAME.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        
        System.out.println("===================================");
        System.out.println("Email: check in the Custom UserDetails Service => " + usernameOrEmail);
        System.out.println("===================================");
        
        Users user = null;

        if (usernameOrEmail.contains("@")) {
            user = userRepository.findByEmail(usernameOrEmail);
        } else {
            user = userRepository.findByUsername(usernameOrEmail);
        }
    
        System.out.println("User Email : " + user.getEmail());
        System.out.println("User Password : " + user.getPassword());
        System.out.println("User AuthProvider : " + user.getAuthProvider());
        System.out.println("Username : " + user.getUsername());
        System.out.println();

        System.out.println("User: " + user);


        if (!user.isEnabled() && user.getAuthProvider() == AuthProvider.LOCAL) { 
            throw new DisabledException("Please verify your email before logging in.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}



// @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
//         System.out.println("===================================");
//         System.out.println("Email: " + email);
//         System.out.println("===================================");
//         Users user = userRepository.findByEmail(email);
//         System.out.println("This is after finding the user in the database");
//         if(user.getAuthProvider().equals(AuthProvider.GOOGLE)) {
//             System.out.println("User: " + user);

//             if (!user.isEnabled() && user.getAuthProvider() == AuthProvider.LOCAL) { 
//                 throw new DisabledException("Please verify your email before logging in.");
//             }

//             return new org.springframework.security.core.userdetails.User(
//                     user.getEmail(),
//                     user.getPassword(),
//                     Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//             );
//         }else{
            
            // System.out.println("User Email : " + user.getEmail());
            // System.out.println("User Password : " + user.getPassword());
            // System.out.println("User AuthProvider : " + user.getAuthProvider());
            // System.out.println("Username : " + user.getUsername());
            // System.out.println();

//             return new org.springframework.security.core.userdetails.User(
//                     user.getEmail(),
//                     user.getPassword(),
//                     Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//             );
//         }
//     }
