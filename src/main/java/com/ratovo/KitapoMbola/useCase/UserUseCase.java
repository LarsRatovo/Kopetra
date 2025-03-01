package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import com.ratovo.KitapoMbola.exception.EmailAlreadyUsedException;
import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.port.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
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
        if(validationCode == null || !validationCode.getValid() || validationCode.getValidatedAt() != null || validationCode.getCheckedAt() != null) throw new InvalidCodeException();
        validationCode.setValidatedAt(Timestamp.from(Instant.now()));
        this.validationCodeRepository.save(validationCode);
    }

    public void createAccount(String wipUuid, String password) {
    }
}
