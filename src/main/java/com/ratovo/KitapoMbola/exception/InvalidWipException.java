package com.ratovo.KitapoMbola.exception;

public class InvalidWipException extends LogicException {

    public static final String defaultMessage = "L' utilisateur 'Work-In-Progress' n'est pas valide";

    public InvalidWipException() {
        super(defaultMessage);
    }
}
