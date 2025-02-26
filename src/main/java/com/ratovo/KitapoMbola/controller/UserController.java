package com.ratovo.KitapoMbola.controller;

import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.exception.LogicException;
import com.ratovo.KitapoMbola.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/wip/{email}")
    public ResponseEntity<?> upsertWipAccount(@PathVariable String email,@Valid @RequestBody WipUpsertRequestDto wip) throws LogicException {
        return userService.upsertWipAccount(email,wip);
    }
}
