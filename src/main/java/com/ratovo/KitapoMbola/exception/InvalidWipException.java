package com.ratovo.KitapoMbola.exception;

public class InvalidWipException extends LogicException {


    @Override
    public String getDefaultMessage() {
        return "Le compte 'Work-In-Progress' n'est pas valide";
    }
}
