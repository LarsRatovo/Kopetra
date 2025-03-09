package com.ratovo.KitapoMbola.service;

import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.useCase.ValidationCodeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationCodeService {

    private final ValidationCodeUseCase useCase;

    public ResponseEntity<?> validate(String targetUuid,String code) throws InvalidCodeException {
        this.useCase.validateCode(targetUuid,code);
        return ResponseEntity.ok().build();
    }
}
