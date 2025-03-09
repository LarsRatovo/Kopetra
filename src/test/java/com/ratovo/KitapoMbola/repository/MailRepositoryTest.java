package com.ratovo.KitapoMbola.repository;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.ratovo.KitapoMbola.adapter.MailAdapter;
import com.ratovo.KitapoMbola.port.MailRepository;
import jakarta.mail.Message;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MailRepositoryTest {

    private static GreenMail greenMail;
    private static JavaMailSenderImpl mailSender;

    @BeforeAll
    public static void initSmtp(){
        greenMail = new GreenMail(ServerSetup.SMTP);
        greenMail.start();

        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(greenMail.getSmtp().getPort());
    }

    @AfterAll
    public static void closeSmtp(){
        greenMail.stop();
    }

    @Test
    @SneakyThrows
    void shouldSendMail(){
        String administratorEmail = "administrator@gmail.com";
        String content = "Hello Mail";
        String subject = "My Subject";
        String destination = "valid@email.com";
        MailRepository mailRepository = new MailAdapter(mailSender,administratorEmail);
        mailRepository.sendMail(subject,content,destination);

        Message message = greenMail.getReceivedMessages()[0];
        assertEquals(administratorEmail,message.getFrom()[0].toString());
        assertEquals(subject, message.getSubject());
        assertEquals(content, message.getContent());
        assertEquals(destination, message.getRecipients(Message.RecipientType.TO)[0].toString());

    }
}
