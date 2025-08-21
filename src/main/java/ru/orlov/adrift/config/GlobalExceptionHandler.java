package ru.orlov.adrift.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.orlov.adrift.controller.dto.ErrorResponseDto;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private String html404 = "404 - Not found";

    public GlobalExceptionHandler(String webappDist) {
        try (InputStream is = new FileInputStream(webappDist + "/404.html")) {
            html404 = new String(is.readAllBytes());
        } catch (Exception ignore) {
        }
    }

    @ExceptionHandler(AppAuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthExceptions(
            AppAuthException ex
    ) {
        HttpStatus status = ex.getHttpStatus() != null
                ? HttpStatus.valueOf(ex.getHttpStatus())
                : HttpStatus.FORBIDDEN;

        return ResponseEntity.status(status)
                .body(ErrorResponseDto.of(ex.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthExceptions(
            MissingRequestHeaderException ex
    ) {
        if (ex.getHeaderName().equals("Authorization")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponseDto.of("Authorization is missing"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.of(ex.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> handleOtherAppExceptions(
            AppException ex
    ) {
        HttpStatus status = ex.getHttpStatus() != null
                ? HttpStatus.valueOf(ex.getHttpStatus())
                : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
                .body(ErrorResponseDto.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Set<String> messages = ex.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toSet());

        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.of(messages));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseDto> handleJsonExceptions(
            HttpMessageNotReadableException ignore
    ) {
        return ResponseEntity.badRequest()
                .body(ErrorResponseDto.of("Invalid JSON request"));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<String> handleNoResourceExceptions(
            NoResourceFoundException ex
    ) {
        if (ex.getResourcePath().startsWith("api/")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseDto.of("Not Found").toJson());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(html404);
        }
    }
}
