package com.ratovo.KitapoMbola.enumeration;

public enum ValidationCodeType {
    REGISTRATION("Code de validation d'inscription"),
    PASSWORD_RESET("Code de validation de mise à jour de mot de passe");

    private final String mailSubject;

    ValidationCodeType(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailSubject() {
        return mailSubject;
    }
}
