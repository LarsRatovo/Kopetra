package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.exception.EmailAlreadyUsedException;
import com.ratovo.KitapoMbola.port.UserRepository;
import com.ratovo.KitapoMbola.port.WipUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserUseCase {
    private final UserRepository userRepository;
    private final WipUserRepository wipRepository;

    public void upsertWipUser(String email, WipUpsertRequestDto dto) throws EmailAlreadyUsedException {
        if(this.userRepository.findByEmail(email) != null) throw new EmailAlreadyUsedException();
        WipUser wipUser = this.wipRepository.findByEmail(email);
        if(wipUser == null) {
            wipUser = new WipUser(dto);
            wipUser.setUuid(UUID.randomUUID().toString());
        }
        this.wipRepository.save(wipUser);
    }
}
