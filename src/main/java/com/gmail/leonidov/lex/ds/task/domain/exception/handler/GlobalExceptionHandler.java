package com.gmail.leonidov.lex.ds.task.domain.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import com.gmail.leonidov.lex.ds.task.web.model.CountryResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.gmail.leonidov.lex.ds.task.domain.exception.AppException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public String handleAppException(AppException e, Model model) {
        CountryResponse errorResponse = e.getErrorResponse();
        model.addAttribute("country", errorResponse);
        return "index";
    }

}
