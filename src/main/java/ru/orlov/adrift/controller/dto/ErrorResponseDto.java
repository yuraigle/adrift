package ru.orlov.adrift.controller.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
@Builder
public class ErrorResponseDto {

    private Set<String> messages;

    public static ErrorResponseDto of(String message) {
        return ErrorResponseDto.builder()
                .messages(Collections.singleton(message)).build();
    }

    public static ErrorResponseDto of(Set<String> messages) {
        return ErrorResponseDto.builder().messages(messages).build();
    }

    public String toJson() {
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
