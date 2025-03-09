package com.ratovo.KitapoMbola.adapter.jpa;

import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.UserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.port.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserRepository {

    private final UserJpaRepository JpaRepository;
    private final SecretKey secretKey;

    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> optional = JpaRepository.findByEmail(email);
        return optional.map(UserMapper::toDomain).orElse(null);
    }

    @Override
    public void createUser(User user) {
        this.JpaRepository.save(UserMapper.toData(user));
    }

    @Override
    public void createLogin(Login login) {
        Optional<UserEntity> optional = JpaRepository.findByEmail(login.getEmail());
        if(optional.isPresent()) {
            UserEntity user = optional.get();
            user.setPassword(encrypt(login.getPassword()));
            this.JpaRepository.save(user);
        }
    }

    public static String encrypt(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return new String(md.digest());
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String login(Login login) {
        Optional<UserEntity> optional = JpaRepository.findByEmailAndPassword((login.getEmail()),encrypt(login.getPassword()));
        if(optional.isEmpty()) return null;
        UserEntity user = optional.get();
        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);
        return Jwts.builder()
                .signWith(secretKey)
                .issuedAt(nowDate)
                .expiration(new Date(now + (24 * 60 * 60 * 1000)))
                .subject(login.getEmail())
                .claims(Map.of("email", login.getEmail(), "uuid", user.getUuid(),"firstName", user.getFirstName(),"lastName", user.getLastName()))
                .compact();
    }

}
