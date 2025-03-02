package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import com.ratovo.KitapoMbola.exception.EmailAlreadyUsedException;
import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.exception.InvalidWipException;
import com.ratovo.KitapoMbola.port.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class UserUseCase {
    private final UserRepository userRepository;
    private final WipUserRepository wipRepository;
    private final ValidationCodeRepository validationCodeRepository;
    private final MailRepository mailRepository;

    @Transactional
    public String upsertWipUser(String email, WipUpsertRequestDto dto) throws EmailAlreadyUsedException {
        if(this.userRepository.findByEmail(email) != null) throw new EmailAlreadyUsedException();
        WipUser wipUser = this.wipRepository.findByEmail(email);
        if(wipUser == null) {
            wipUser = new WipUser(dto);
            wipUser.setUuid(UUID.randomUUID().toString());
        }
        this.wipRepository.save(wipUser);
        ValidationCode registrationCode = this.validationCodeRepository.generateCode(ValidationCodeType.REGISTRATION);
        registrationCode.setTargetUuid(wipUser.getUuid());
        this.validationCodeRepository.invalidateCodeSentTo(wipUser.getUuid());
        this.mailRepository.sendMail(ValidationCodeType.REGISTRATION.getMailSubject(), wipUser.getEmail(),ValidationCodeRepository.getMailContent(registrationCode.getCode()));
        this.validationCodeRepository.save(registrationCode);
        return wipUser.getUuid();
    }

    public void validateCode(String uuid,String code) throws InvalidCodeException {
        ValidationCode validationCode = this.validationCodeRepository.findByCode(code,uuid);
        Timestamp now = Timestamp.from(Instant.now());
        if(validationCode == null || validationCode.getValidatedAt() != null) throw new InvalidCodeException();
        LocalDateTime validationDeadLine = validationCode.getCreatedAt().toLocalDateTime().plusMinutes(30);
        if(now.toLocalDateTime().isAfter(validationDeadLine)) throw new InvalidCodeException();
        validationCode.setValidatedAt(now);
        this.validationCodeRepository.save(validationCode);
    }

    @Transactional
    public void createAccount(String wipUuid, String password) throws InvalidWipException {
        WipUser wipUser = this.wipRepository.findByUuid(wipUuid);
        if(wipUser == null) throw new InvalidWipException();
        ValidationCode registrationCode = this.validationCodeRepository.findValidatedCodeByTargetUuid(wipUuid);
        if( registrationCode == null || registrationCode.getType() != ValidationCodeType.REGISTRATION || registrationCode.getValidatedAt() == null) {
            throw new InvalidWipException();
        }
        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .email(wipUser.getEmail())
                .firstName(wipUser.getFirstName())
                .lastName(wipUser.getLastName())
                .build();
        this.userRepository.createUser(user);
        this.userRepository.createLogin(Login.builder()
                        .email(user.getEmail())
                        .password(password)
                .build());
        this.validationCodeRepository.invalidateCodeSentTo(wipUuid);
        this.wipRepository.removeWipAccount(wipUuid);
    }
}
