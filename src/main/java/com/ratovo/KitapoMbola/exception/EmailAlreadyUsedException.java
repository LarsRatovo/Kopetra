package com.ratovo.KitapoMbola.exception;

public class EmailAlreadyUsedException extends LogicException {
    public static String defaultMessage = "L'email est déjà utilisé";

    public EmailAlreadyUsedException() {
        super(defaultMessage);
    }
}
