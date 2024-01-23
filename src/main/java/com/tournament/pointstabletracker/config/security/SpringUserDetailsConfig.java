package com.tournament.pointstabletracker.config.security;

import com.tournament.pointstabletracker.entity.user.AppUser;
import com.tournament.pointstabletracker.repository.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class SpringUserDetailsConfig {

    private final AppUserRepository appUserRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> appUserRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
