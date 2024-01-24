package com.tournament.pointstabletracker.controller;

import com.tournament.pointstabletracker.dto.CommonApiResponse;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationRequest;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationResponse;
import com.tournament.pointstabletracker.dto.auth.AppRegistrationRequest;
import com.tournament.pointstabletracker.service.auth.AppAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AppAuthenticationController {

    private final AppAuthenticationService appAuthenticationService;

    public AppAuthenticationController(AppAuthenticationService appAuthenticationService) {
        this.appAuthenticationService = appAuthenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> register(@Valid @RequestBody AppRegistrationRequest appUserRegistrationRequest) {

        AppAuthenticationResponse appAuthenticationResponse = appAuthenticationService.register(appUserRegistrationRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonApiResponse<>("true", appAuthenticationResponse, null));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> authenticate(@Valid @RequestBody AppAuthenticationRequest appUserRegistrationRequest) {

        AppAuthenticationResponse appAuthenticationResponse = appAuthenticationService.authenticate(appUserRegistrationRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonApiResponse<>("true", appAuthenticationResponse, null));
    }
}
