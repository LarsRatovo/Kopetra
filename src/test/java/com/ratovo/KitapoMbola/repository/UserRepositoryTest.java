package com.ratovo.KitapoMbola.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratovo.KitapoMbola.adapter.jpa.UserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.UserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.fixture.UserFixture;
import com.ratovo.KitapoMbola.port.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserJpaRepository jpaRepository;

    @Test
    void shouldReturnExistingUserWithEmail(){
        this.jpaRepository.save(UserMapper.toData(UserFixture.user1));
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository,null);
        User foundUser = userRepository.findByEmail(UserFixture.user1.getEmail());
        assertEquals(UserFixture.user1,foundUser);
    }

    @Test
    void shouldCreateNewUser(){
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository,null);
        userRepository.createUser(UserFixture.user1);
        assertEquals(jpaRepository.findByEmail(UserFixture.user1.getEmail()).map(UserMapper::toDomain).orElse(null), UserFixture.user1);
    }

    @Test
    @SneakyThrows
    void shouldCreateLogin(){
        this.jpaRepository.save(UserMapper.toData(UserFixture.user1));
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository,null);
        userRepository.createLogin(UserFixture.login1);
        UserEntity user = this.jpaRepository.findByEmail(UserFixture.user1.getEmail()).orElse(null);
        assertTrue(
                user != null &&
                user.getEmail().equals(UserFixture.user1.getEmail()) &&
                user.getPassword().equals(UserDatabaseAdapter.encrypt(UserFixture.login1.getPassword()))
        );
    }


    @Test
    @SneakyThrows
    void shouldLogin(){
        UserEntity userEntity = UserMapper.toData(UserFixture.user1);
        userEntity.setPassword(UserDatabaseAdapter.encrypt(UserFixture.login1.getPassword()));
        this.jpaRepository.save(userEntity);
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository, Jwts.SIG.HS256.key().build());
        String token = userRepository.login(UserFixture.login1);
        JsonNode node = new ObjectMapper().readTree(Base64.getDecoder().decode(token.split("\\.")[1]));
        assertEquals(node.get("email").textValue(), UserFixture.user1.getEmail());
        assertEquals(node.get("uuid").textValue(), UserFixture.user1.getUuid());
        assertEquals(node.get("firstName").textValue(), UserFixture.user1.getFirstName());
        assertEquals(node.get("lastName").textValue(), UserFixture.user1.getLastName());
    }
}
