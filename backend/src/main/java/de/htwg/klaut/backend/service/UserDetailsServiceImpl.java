package de.htwg.klaut.backend.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new User("test", "$2a$04$jQAmSFOWuZ7QwEoPX.mzXO4Fv/KGhJBTrCPA3c3io.96wTPUvU9l2",
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
}
