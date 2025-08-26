package ru.orlov.adrift.config;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.orlov.adrift.controller.dto.ErrorResponseDto;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(AppAuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthExceptions(
            AppAuthException ex
    ) {
        HttpStatus status = ex.getHttpStatus() != null
                ? HttpStatus.valueOf(ex.getHttpStatus())
                : HttpStatus.FORBIDDEN;

        return ResponseEntity.status(status)
                .header("Content-Type", "application/json")
                .header("Vary", "Origin")
                .body(ErrorResponseDto.of(ex.getMessage()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthExceptions(
            MissingRequestHeaderException ex
    ) {
        if (ex.getHeaderName().equals("Authorization")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json")
                    .header("Vary", "Origin")
                    .body(ErrorResponseDto.of("Authorization is missing"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", "application/json")
                .header("Vary", "Origin")
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
                .header("Content-Type", "application/json")
                .header("Vary", "Origin")
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
                .header("Content-Type", "application/json")
                .header("Vary", "Origin")
                .body(ErrorResponseDto.of(messages));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseDto> handleJsonExceptions(
            HttpMessageNotReadableException ignore
    ) {
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json")
                .header("Vary", "Origin")
                .body(ErrorResponseDto.of("Invalid JSON request"));
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    protected ResponseEntity<String> handleNoResourceExceptions(
            ServletException ignore
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "application/json")
                .header("Vary", "Origin")
                .body(ErrorResponseDto.of("Not Found").toJson());
    }
}
