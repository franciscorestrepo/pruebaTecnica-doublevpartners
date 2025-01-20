package com.doublevpartners.pruebatecnica.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return User.builder()
                .username("admin")
                .password("{noop}861210")
                .authorities(Collections.emptyList())
                .build();
    }
}
