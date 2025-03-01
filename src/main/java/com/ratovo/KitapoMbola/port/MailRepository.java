package com.ratovo.KitapoMbola.port;

public interface MailRepository {
    void sendMail(String subject,String content,String email);
}
