package com.ratovo.KitapoMbola.exception;

public abstract class LogicException extends Exception {

    public LogicException() {
        super();
    }

    public abstract String getDefaultMessage();
}
