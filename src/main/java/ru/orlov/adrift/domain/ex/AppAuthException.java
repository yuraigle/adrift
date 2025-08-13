package ru.orlov.adrift.domain.ex;

import org.springframework.http.HttpStatus;

public class AppAuthException extends AppException {

    public AppAuthException(String message) {
        super(message);
    }

    public AppAuthException(String message, HttpStatus httpStatus) {
        super(message, httpStatus.value());
    }

}
