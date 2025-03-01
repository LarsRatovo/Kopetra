package com.ratovo.KitapoMbola.adapter;

import com.ratovo.KitapoMbola.port.MailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@RequiredArgsConstructor
public class MailAdapter implements MailRepository {

    private final JavaMailSender mailSender;
    private final String administratorEmail;

    @Override
    public void sendMail(String subject, String content, String email) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setFrom(administratorEmail);
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSubject(subject);
            mailSender.send(mimeMessage);

        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }
}
