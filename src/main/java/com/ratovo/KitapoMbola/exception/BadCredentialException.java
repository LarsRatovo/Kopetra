package com.ratovo.KitapoMbola.exception;

public class BadCredentialException extends LogicException{

    @Override
    public String getDefaultMessage() {
        return "Le nom d'utilisateur ou le mot de passe est incorrect.";
    }
}
