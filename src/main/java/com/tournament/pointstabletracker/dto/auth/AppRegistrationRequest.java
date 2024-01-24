package com.tournament.pointstabletracker.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppRegistrationRequest {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
