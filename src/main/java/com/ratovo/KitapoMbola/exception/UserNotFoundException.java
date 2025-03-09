package com.ratovo.KitapoMbola.exception;

public class UserNotFoundException extends LogicException {

    @Override
    public String getDefaultMessage() {
        return "L'utilisateur n'existe pas";
    }
}
