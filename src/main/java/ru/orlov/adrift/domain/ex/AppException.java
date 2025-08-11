package ru.orlov.adrift.domain.ex;

import lombok.Getter;
import lombok.Setter;

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

}
