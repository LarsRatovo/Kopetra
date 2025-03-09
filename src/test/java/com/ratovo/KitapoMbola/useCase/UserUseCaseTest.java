package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import com.ratovo.KitapoMbola.exception.BadCredentialException;
import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.exception.InvalidWipException;
import com.ratovo.KitapoMbola.fixture.UserFixture;
import com.ratovo.KitapoMbola.fixture.ValidationCodeFixture;
import com.ratovo.KitapoMbola.fixture.WipFixture;
import com.ratovo.KitapoMbola.port.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserUseCaseTest {
    private final WipUserRepository wipRepository;
    private final UserRepository userRepository;
    private final UserUseCase useCase;
    private final ValidationCodeRepository validationCodeRepository;
    private final MailRepository mailRepository;

    public UserUseCaseTest(){
        this.wipRepository = mock(WipUserRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.validationCodeRepository = mock(ValidationCodeRepository.class);
        this.mailRepository = mock(MailRepository.class);
        this.useCase = new UserUseCase(userRepository,wipRepository,validationCodeRepository,mailRepository);

    }

    @Test
    @SneakyThrows
    void shouldCreateWipUser(){
        UUID uuid = UUID.randomUUID();
        when(validationCodeRepository.generateCode(eq(ValidationCodeType.REGISTRATION))).thenReturn(ValidationCodeFixture.ok);
        doNothing().when(wipRepository).save(any(WipUser.class));
        try (MockedStatic<UUID> mockedUuid = mockStatic(UUID.class)) {
            mockedUuid.when(UUID::randomUUID).thenReturn(uuid);
            String createdUuid = this.useCase.upsertWipUser(WipFixture.validWip.getEmail(),WipFixture.validWip);
            verify(wipRepository).save(any(WipUser.class));
            assertEquals(uuid.toString(),createdUuid);
        }
    }

    @Test
    @SneakyThrows
    void shouldSendAndSaveValidationCode(){
        when(validationCodeRepository.generateCode(eq(ValidationCodeType.REGISTRATION))).thenReturn(ValidationCodeFixture.ok);
        doNothing().when(validationCodeRepository).save(any(ValidationCode.class));
        String uuid = this.useCase.upsertWipUser(WipFixture.validWip.getEmail(),WipFixture.validWip);

        verify(mailRepository).sendMail(ValidationCodeType.REGISTRATION.getMailSubject(), WipFixture.validWip.getEmail(),ValidationCodeRepository.getMailContent(ValidationCodeFixture.ok.getCode()));
        verify(validationCodeRepository).invalidateCodeSentTo(uuid);
        verify(validationCodeRepository).save(argThat(
                validationCode ->
                        Objects.equals(uuid,validationCode.getTargetUuid()) &&
                                Objects.equals(validationCode.getCode(),ValidationCodeFixture.ok.getCode()) &&
                                Objects.equals(validationCode.getType(),ValidationCodeType.REGISTRATION)
        ));
    }

    @Test
    @SneakyThrows
    void shouldUpdateExistingWipUser(){
        when(validationCodeRepository.generateCode(eq(ValidationCodeType.REGISTRATION))).thenReturn(ValidationCodeFixture.ok);
        when(wipRepository.findByEmail(anyString())).thenReturn(WipFixture.wip1);
        doNothing().when(wipRepository).save(any(WipUser.class));
        WipUpsertRequestDto updateWip =  WipUpsertRequestDto
                .builder()
                .email(WipFixture.wip1.getEmail())
                .lastName("Last name updated")
                .firstName("First name updated")
                .build();
        this.useCase.upsertWipUser(WipFixture.validWip.getEmail(), updateWip);
        verify(wipRepository).findByEmail(anyString());
        verify(wipRepository).save(argThat(wip ->
                Objects.equals(wip.getUuid(), WipFixture.wip1.getUuid()) &&
                Objects.equals(wip.getLastName(), WipFixture.wip1.getLastName()) &&
                Objects.equals(wip.getFirstName(), WipFixture.wip1.getFirstName())
        ));
    }

    @Test
    @SneakyThrows
    void shouldValidateCode(){
        when(validationCodeRepository.findByCode(eq(ValidationCodeFixture.code3.getCode()),eq(ValidationCodeFixture.validTargetUuid))).thenReturn(new ValidationCode(ValidationCodeFixture.code3));
        this.useCase.validateCode(ValidationCodeFixture.validTargetUuid,ValidationCodeFixture.code3.getCode());
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

    @Test
    @SneakyThrows
    void shouldCreateUser(){
        when(this.wipRepository.findByUuid(WipFixture.wip1.getUuid())).thenReturn(WipFixture.wip1);
        when(this.validationCodeRepository.findValidatedCodeByTargetUuid(WipFixture.wip1.getUuid())).thenReturn(new ValidationCode(ValidationCodeFixture.code2));
        UUID userUuid = UUID.randomUUID();
        try (MockedStatic<UUID> uuidMock = Mockito.mockStatic(UUID.class)) {
            uuidMock.when(UUID::randomUUID).thenReturn(userUuid);
            this.useCase.createAccount(WipFixture.wip1.getUuid(),"abcdeF1;");
        }

        verify(this.validationCodeRepository).invalidateCodeSentTo(argThat(arg -> arg.equals(WipFixture.wip1.getUuid())));
        verify(this.wipRepository).removeWipAccount(argThat( arg -> arg.equals(WipFixture.wip1.getUuid())));
        verify(this.userRepository).createUser(argThat( arg ->
            arg.getEmail().equals(WipFixture.wip1.getEmail()) &&
            arg.getUuid().equals(userUuid.toString()) &&
            arg.getFirstName().equals(WipFixture.wip1.getFirstName()) &&
            arg.getLastName().equals(WipFixture.wip1.getLastName())
        ));
        verify(this.userRepository).createLogin(argThat( arg->
            arg.getEmail().equals(WipFixture.wip1.getEmail()) &&
            arg.getPassword().equals("abcdeF1;")
        ));
    }

    @Test
    @SneakyThrows
    void shouldThrowInvalidWipExceptionByCodeVerification(){
        when(this.wipRepository.findByUuid(WipFixture.wip1.getUuid())).thenReturn(WipFixture.wip1);
        when(this.validationCodeRepository.findValidatedCodeByTargetUuid(ValidationCodeFixture.code3.getTargetUuid())).thenReturn(new ValidationCode(ValidationCodeFixture.code3));
        assertThrows(InvalidWipException.class,()->{
            this.useCase.createAccount(WipFixture.wip1.getUuid(),"abcdeF1;");
        });
    }

    @Test
    @SneakyThrows
    void shouldThrowInvalidWipMandatoryDataException(){
        when(this.wipRepository.findByUuid(WipFixture.wip1.getUuid())).thenReturn(WipFixture.wip2);
        assertThrows(InvalidWipException.class,()->{
            this.useCase.createAccount(WipFixture.wip1.getUuid(),"abcdeF1;");
        });
    }

    @Test
    @SneakyThrows
    void shouldLogIn(){
        when(this.userRepository.login(eq(UserFixture.login1))).thenReturn(UserFixture.tokenFixture);
        assertEquals(UserFixture.tokenFixture,this.useCase.login(UserFixture.login1.getEmail(),UserFixture.login1.getPassword()));
    }

    @Test
    @SneakyThrows
    void shouldThrowBadCredentialException(){
        when(this.userRepository.login(eq(UserFixture.login1))).thenReturn(null);
        assertThrows(BadCredentialException.class,() -> this.useCase.login(UserFixture.login1.getEmail(),UserFixture.login1.getPassword()));
    }
}
