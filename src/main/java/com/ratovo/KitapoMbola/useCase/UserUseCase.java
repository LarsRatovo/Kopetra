package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.*;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import com.ratovo.KitapoMbola.exception.*;
import com.ratovo.KitapoMbola.port.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
        ValidationCode registrationCode = this.validationCodeRepository.generateCode(ValidationCodeType.REGISTRATION,wipUser.getUuid());
        this.mailRepository.sendMail(ValidationCodeType.REGISTRATION.getMailSubject(), wipUser.getEmail(),ValidationCodeRepository.getMailContent(registrationCode.getCode(),ValidationCodeType.REGISTRATION));
        this.validationCodeRepository.save(registrationCode);
        return wipUser.getUuid();
    }

    @Transactional
    public void createAccount(String wipUuid, String password) throws InvalidWipException {
        WipUser wipUser = this.wipRepository.findByUuid(wipUuid);
        validateWipUser(wipUser);
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
        this.userRepository.saveLogin(Login.builder()
                        .email(user.getEmail())
                        .password(password)
                .build());
        this.validationCodeRepository.invalidateCodeSentTo(wipUuid);
        this.wipRepository.removeWipAccount(wipUuid);
    }

    private void validateWipUser(WipUser wipUser) throws InvalidWipException {
        if(wipUser == null) throw new InvalidWipException();
        if(wipUser.getEmail() == null || wipUser.getEmail().isEmpty()) throw new InvalidWipException();
        if(wipUser.getFirstName() == null || wipUser.getFirstName().isEmpty()) throw new InvalidWipException();
        if(wipUser.getLastName() == null || wipUser.getLastName().isEmpty()) throw new InvalidWipException();
    }

    public String login(String username, String password) throws BadCredentialException {
        String token = this.userRepository.login(Login.builder().email(username).password(password).build());
        if(token == null) throw new BadCredentialException();
        return token;
    }

    public String forgotPassword(String email) throws UserNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if(user == null) throw new UserNotFoundException();
        ValidationCode resetPasswordCode = this.validationCodeRepository.generateCode(ValidationCodeType.PASSWORD_RESET,user.getUuid());
        this.mailRepository.sendMail(ValidationCodeType.PASSWORD_RESET.getMailSubject(), user.getEmail(),ValidationCodeRepository.getMailContent(resetPasswordCode.getCode(),ValidationCodeType.PASSWORD_RESET));
        this.validationCodeRepository.save(resetPasswordCode);
        return user.getUuid();
    }

    public void resetPassword(String userUuid,String password) throws UserNotFoundException,InvalidCodeException {
        User user = this.userRepository.findByUuid(userUuid);
        if(user == null) throw new UserNotFoundException();
        ValidationCode resetPasswordCode = this.validationCodeRepository.findValidatedCodeByTargetUuid(userUuid);
        if(resetPasswordCode == null || resetPasswordCode.getType() != ValidationCodeType.PASSWORD_RESET || resetPasswordCode.getValidatedAt() == null) throw new InvalidCodeException();
        this.userRepository.saveLogin(Login.builder()
                .email(user.getEmail())
                .password(password)
                .build());
        this.validationCodeRepository.invalidateCodeSentTo(userUuid);
    }
}
