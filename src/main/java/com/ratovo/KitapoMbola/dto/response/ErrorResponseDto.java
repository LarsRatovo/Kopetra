package com.ratovo.KitapoMbola.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResponseDto {
    private String exceptionMessage;
    private List<String> messages;
}
