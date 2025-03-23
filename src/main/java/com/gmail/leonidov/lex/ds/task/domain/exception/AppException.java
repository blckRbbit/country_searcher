package com.gmail.leonidov.lex.ds.task.domain.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.gmail.leonidov.lex.ds.task.web.model.CountryResponse;

@Slf4j
@Getter
public class AppException extends RuntimeException {
    private final CountryResponse errorResponse;

    public AppException(CountryResponse errorResponse) {
        super(errorResponse.toString());
        this.errorResponse = errorResponse;
    }

}
