package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.port.ValidationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ValidationCodeUseCase {

    private final ValidationCodeRepository repository;

    public void validateCode(String targetUuid,String code) throws InvalidCodeException {
        ValidationCode validationCode = this.repository.findByCode(code,targetUuid);
        Timestamp now = Timestamp.from(Instant.now());
        if(validationCode == null || validationCode.getValidatedAt() != null) throw new InvalidCodeException();
        LocalDateTime validationDeadLine = validationCode.getCreatedAt().toLocalDateTime().plusMinutes(30);
        if(now.toLocalDateTime().isAfter(validationDeadLine)) throw new InvalidCodeException();
        validationCode.setValidatedAt(now);
        this.repository.save(validationCode);
    }
}
