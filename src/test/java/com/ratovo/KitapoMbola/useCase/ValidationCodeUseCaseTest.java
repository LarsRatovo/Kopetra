package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.fixture.ValidationCodeFixture;
import com.ratovo.KitapoMbola.port.ValidationCodeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ValidationCodeUseCaseTest {
    private final ValidationCodeRepository validationCodeRepository;

    private final ValidationCodeUseCase useCase;

    public ValidationCodeUseCaseTest(){
        this.validationCodeRepository = mock(ValidationCodeRepository.class);
        this.useCase = new ValidationCodeUseCase(validationCodeRepository);
    }
    @Test
    @SneakyThrows
    void shouldValidateCode(){
        when(validationCodeRepository.findByCode(eq(ValidationCodeFixture.ok.getCode()),eq(ValidationCodeFixture.validTargetUuid))).thenReturn(ValidationCodeFixture.ok);
        this.useCase.validateCode(ValidationCodeFixture.validTargetUuid,ValidationCodeFixture.ok.getCode());
        verify(validationCodeRepository).save(argThat(
                validationCode ->
                        validationCode.getTargetUuid().equals(ValidationCodeFixture.validTargetUuid) &&
                                validationCode.getCreatedAt().before(validationCode.getValidatedAt())
        ));
    }

    @Test
    @SneakyThrows
    void shouldInValidateCodeWhenNull(){
        when(validationCodeRepository.findByCode(anyString(),anyString())).thenReturn(null);
        assertThrows(InvalidCodeException.class, () -> this.useCase.validateCode(anyString(),anyString()));
    }

    @Test
    @SneakyThrows
    void shouldInvalidateCodeWhenAlreadyValidated(){
        when(validationCodeRepository.findByCode(anyString(),anyString())).thenReturn(ValidationCodeFixture.code2);
        assertThrows(InvalidCodeException.class, () -> this.useCase.validateCode(ValidationCodeFixture.code2.getTargetUuid(),ValidationCodeFixture.code2.getCode()));
    }
}
