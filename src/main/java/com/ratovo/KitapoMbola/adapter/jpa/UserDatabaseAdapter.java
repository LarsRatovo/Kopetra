package com.ratovo.KitapoMbola.adapter.jpa;

import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.UserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.port.UserRepository;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserRepository {

    private final UserJpaRepository JpaRepository;

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
            try {
                UserEntity user = optional.get();
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(login.getPassword().getBytes());
                user.setPassword(new String(digest.digest()));
                this.JpaRepository.save(user);
            }catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
