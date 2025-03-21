package com.ratovo.KitapoMbola.service;

import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.dto.response.ResetPasswordResponseDto;
import com.ratovo.KitapoMbola.dto.response.UpsertWipResponseDto;
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
        String uuid = userUseCase.upsertWipUser(email,wip);
        UpsertWipResponseDto responseDto = new UpsertWipResponseDto(uuid);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createAccountBasedOnWip(String wip,String password) throws LogicException {
        this.userUseCase.createAccount(wip,password);
        return new ResponseEntity<>(new UpsertWipResponseDto(wip),HttpStatus.OK);
    }

    public ResponseEntity<?> login(String username, String password) throws LogicException {
        return new ResponseEntity<>(this.userUseCase.login(username,password),HttpStatus.OK);
    }

    public ResponseEntity<?> resetPassword(String username) throws LogicException {
        ResetPasswordResponseDto dto = new ResetPasswordResponseDto(this.userUseCase.forgotPassword(username));
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<?> updatePassword(String userUuid,String password) throws LogicException{
        this.userUseCase.resetPassword(userUuid,password);
        return ResponseEntity.ok().build();
    }
}
