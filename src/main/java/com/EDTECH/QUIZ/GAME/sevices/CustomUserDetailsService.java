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

    // @Override 
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     Users user = userRepository.findByUsername(username);
    //     user.setAuthProvider(Users.AuthProvider.GOOGLE);
    //     System.out.println("This is in the user details service: " + user);
    //     String p = "";
    //     if(user.getPassword()==null){
    //         p = "";
    //     }else{
    //         p = user.getPassword();
    //     }

    //     if (!user.isEnabled()) {
    //         throw new DisabledException("Please verify your email before logging in.");
    //     }
        
    //     return User.builder()
    //         .username(user.getUsername())
    //         .password(p) 
    //         .build();
    // }

    @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = userRepository.findByUsername(username);
    
    if (user == null) {
        throw new UsernameNotFoundException("User not found.");
    }

    if (!user.isEnabled() && user.getAuthProvider() == AuthProvider.LOCAL) { 
        throw new DisabledException("Please verify your email before logging in.");
    }

    return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
    );
}

}
