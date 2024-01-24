package com.tournament.pointstabletracker.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppAuthenticationRequest {
    private String userName;
    private String password;
}
