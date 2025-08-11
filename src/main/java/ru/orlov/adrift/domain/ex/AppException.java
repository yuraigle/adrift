package ru.orlov.adrift.domain.ex;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AppException extends Exception {

    private Integer httpStatus = 500;

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Integer httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus.value();
    }
}
