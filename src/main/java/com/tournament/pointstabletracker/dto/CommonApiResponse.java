package com.tournament.pointstabletracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonApiResponse<T> {

    private String isSuccess;

    private T data;

    private ErrorDetails errorDetails;

}
