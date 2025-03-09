package com.ratovo.KitapoMbola.exception;

public class InvalidCodeException extends LogicException {

    @Override
    public String getDefaultMessage() {
        return "Le code de validation est invalide";
    }
}
