package com.ratovo.KitapoMbola.configuration;

import com.ratovo.KitapoMbola.adapter.MailAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.UserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.ValidationCodeDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.WipUserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.adapter.jpa.repository.ValidationCodeJpaRepository;
import com.ratovo.KitapoMbola.adapter.jpa.repository.WipUserJpaRepository;
import com.ratovo.KitapoMbola.port.*;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import javax.crypto.SecretKey;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public WipUserRepository getWipRepository(WipUserJpaRepository wipRepository) {
        return new WipUserDatabaseAdapter(wipRepository);
    }

    @Bean
    public SecretKey jwtSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }

    @Bean
    public UserRepository getUserRepository(UserJpaRepository userRepository,SecretKey jwtSecretKey) {
        return new UserDatabaseAdapter(userRepository,jwtSecretKey);
    }

    @Bean
    public ValidationCodeRepository getValidationCode(ValidationCodeJpaRepository validationCodeRepository) {
        return new ValidationCodeDatabaseAdapter(validationCodeRepository);
    }

    @Bean
    public MailRepository mailRepository(JavaMailSender mailSender,@Value("${spring.mail.username}") String administratorEmail) {
        return new MailAdapter(mailSender,administratorEmail);
    }
}
