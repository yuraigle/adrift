package ru.orlov.adrift.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppAuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthExceptions(AppAuthException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.of(ex.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> handleAppExceptions(AppException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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

    @Data
    @Builder
    public static class ErrorResponseDto {
        private Set<String> messages;

        public static ErrorResponseDto of(String message) {
            return ErrorResponseDto.builder()
                    .messages(Collections.singleton(message)).build();
        }

        public static ErrorResponseDto of(Set<String> messages) {
            return ErrorResponseDto.builder().messages(messages).build();
        }
    }

}
