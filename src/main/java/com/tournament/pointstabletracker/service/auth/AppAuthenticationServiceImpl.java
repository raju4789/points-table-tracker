package com.tournament.pointstabletracker.service.auth;

import com.tournament.pointstabletracker.dto.auth.AppAuthenticationRequest;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationResponse;
import com.tournament.pointstabletracker.dto.auth.AppRegistrationRequest;
import com.tournament.pointstabletracker.entity.user.AppUser;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import com.tournament.pointstabletracker.repository.user.AppUserRepository;
import com.tournament.pointstabletracker.service.security.JWTService;
import com.tournament.pointstabletracker.utils.ApplicationConstants.AppRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppAuthenticationServiceImpl implements AppAuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AppAuthenticationResponse authenticate(AppAuthenticationRequest appAuthenticationRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(appAuthenticationRequest.getUserName(), appAuthenticationRequest.getPassword())
        );

        AppUser user = appUserRepository.findById(appAuthenticationRequest.getUserName())
                .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + appAuthenticationRequest.getUserName()));


        String JWTToken = jwtService.generateToken(user);

        return AppAuthenticationResponse.builder()
                .token(JWTToken)
                .build();
    }

    @Override
    public AppAuthenticationResponse register(AppRegistrationRequest appRegistrationRequest) {
        AppUser user = AppUser.builder()
                .username(appRegistrationRequest.getUserName())
                .password(passwordEncoder.encode(appRegistrationRequest.getPassword()))
                .email(appRegistrationRequest.getEmail())
                .firstName(appRegistrationRequest.getFirstName())
                .lastName(appRegistrationRequest.getLastName())
                .role(AppRole.USER)
                .build();

        appUserRepository.save(user);

        String JWTToken = jwtService.generateToken(user);

        return AppAuthenticationResponse.builder()
                .token(JWTToken)
                .build();
    }
}
