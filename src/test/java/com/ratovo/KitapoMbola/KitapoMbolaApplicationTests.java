package com.ratovo.KitapoMbola;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureDataJpa
class KitapoMbolaApplicationTests {

    private static GreenMail greenMail;

    @BeforeAll
    public static void startGreenMail() {
        greenMail = new GreenMail(ServerSetup.SMTP);
        greenMail.setUser("admin@localhost.com", "admin", "admin");
        greenMail.start();
        System.setProperty("spring.mail.host","localhost");
        System.setProperty("spring.mail.port", String.valueOf(greenMail.getSmtp().getPort()));
        System.setProperty("spring.mail.protocol", "smtp");
        System.setProperty("spring.mail.username", "admin@localhost.com");
        System.setProperty("spring.mail.password", "admin");
    }

    @AfterAll
    public static void stopGreenMail() {
        greenMail.stop();
    }

    @Test
    void contextLoads() {
    }

}