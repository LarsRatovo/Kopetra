package com.ratovo.KitapoMbola.controller;

import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.exception.LogicException;
import com.ratovo.KitapoMbola.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PutMapping("/wip/{email}")
    public ResponseEntity<?> upsertWipAccount(@PathVariable String email,@Valid @RequestBody WipUpsertRequestDto wip) throws LogicException {
        return userService.upsertWipAccount(email,wip);
    }

    @PostMapping("/wip/{uuid}/validate")
    public ResponseEntity<?> validateWipAccount(@PathVariable String uuid,@RequestParam String code) throws LogicException {
        return userService.validateCode(uuid,code);
    }

    @PostMapping
    public ResponseEntity<?> createAccount(
            @RequestParam String wip,
            @RequestParam @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*\\W).{8,}",message = "Le mot de passe est invalide") String password) throws LogicException {
        return userService.createAccountBasedOnWip(wip,password);
    }
}
