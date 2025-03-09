package com.ratovo.KitapoMbola.exception;

public class EmailAlreadyUsedException extends LogicException {

    @Override
    public String getDefaultMessage() {
        return "L'email est déjà utilisé";
    }
}
