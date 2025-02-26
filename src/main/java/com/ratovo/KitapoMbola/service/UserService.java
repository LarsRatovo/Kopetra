package com.ratovo.KitapoMbola.service;

import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.exception.LogicException;
import com.ratovo.KitapoMbola.useCase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserUseCase userUseCase;

    public ResponseEntity<?> upsertWipAccount(String email, WipUpsertRequestDto wip) throws LogicException {
        userUseCase.upsertWipUser(email,wip);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
