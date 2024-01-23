package com.tournament.pointstabletracker.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidRequestException extends Exception {

    public InvalidRequestException(String message) {
        super(message);
    }

}
