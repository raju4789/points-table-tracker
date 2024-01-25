package com.tournament.pointstabletracker.exceptions;

public class UserNotAuthenticationException extends RuntimeException {

        public UserNotAuthenticationException(String message) {
            super(message);
        }
}
