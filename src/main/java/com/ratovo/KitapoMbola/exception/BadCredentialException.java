package com.ratovo.KitapoMbola.exception;

public class BadCredentialException extends LogicException{

    public static final String defaultMessage = "Le nom d'utilisateur ou le mot de passe est incorrect.";

    public BadCredentialException() {
        super(defaultMessage);
    }
}
