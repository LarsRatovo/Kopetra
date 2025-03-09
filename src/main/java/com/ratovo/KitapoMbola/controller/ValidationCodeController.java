package com.ratovo.KitapoMbola.controller;

import com.ratovo.KitapoMbola.exception.LogicException;
import com.ratovo.KitapoMbola.service.ValidationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation-code")
@RequiredArgsConstructor
public class ValidationCodeController {

    private final ValidationCodeService service;

    @PostMapping("/validate")
    public ResponseEntity<?> validateCode(@RequestParam String targetUuid,@RequestParam String code) throws LogicException {
        return service.validate(targetUuid,code);
    }
}
