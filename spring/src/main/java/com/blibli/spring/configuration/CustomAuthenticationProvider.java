package com.blibli.spring.configuration;

import com.blibli.spring.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService memberDetailsService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken
                .class.equals(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username;
            username = jwtService.verifyToken((String) authentication.getCredentials());

        UserDetails user;
        try {
            user = memberDetailsService.loadUserByUsername(username);
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }

}
