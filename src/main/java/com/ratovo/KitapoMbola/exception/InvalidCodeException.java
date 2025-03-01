package com.ratovo.KitapoMbola.exception;

public class InvalidCodeException extends LogicException {
    public final static String defaultMessage = "Le code de validation est invalide";
    public InvalidCodeException() {
        super(defaultMessage);
    }
}
