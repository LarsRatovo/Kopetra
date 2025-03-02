package com.ratovo.KitapoMbola.repository;

import com.ratovo.KitapoMbola.adapter.jpa.UserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.UserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.fixture.UserFixture;
import com.ratovo.KitapoMbola.port.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserJpaRepository jpaRepository;

    @Test
    void shouldReturnExistingUserWithEmail(){
        this.jpaRepository.save(UserMapper.toData(UserFixture.user1));
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository);
        User foundUser = userRepository.findByEmail(UserFixture.user1.getEmail());
        assertEquals(UserFixture.user1,foundUser);
    }

    @Test
    void shouldCreateNewUser(){
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository);
        userRepository.createUser(UserFixture.user1);
        assertEquals(jpaRepository.findByEmail(UserFixture.user1.getEmail()).map(UserMapper::toDomain).orElse(null), UserFixture.user1);
    }

    @Test
    @SneakyThrows
    void shouldCreateLogin(){
        this.jpaRepository.save(UserMapper.toData(UserFixture.user1));
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository);
        userRepository.createLogin(Login.builder()
                        .email(UserFixture.user1.getEmail())
                        .password("password")
                .build());
        UserEntity user = this.jpaRepository.findByEmail(UserFixture.user1.getEmail()).orElse(null);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("password".getBytes());
        assertTrue(
                user != null &&
                user.getEmail().equals(UserFixture.user1.getEmail()) &&
                user.getPassword().equals(new String(md.digest()))
        );
    }
}
