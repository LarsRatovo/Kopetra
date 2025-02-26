package com.ratovo.KitapoMbola.adapter.jpa;

import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.UserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.port.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserRepository {

    private final UserJpaRepository JpaRepository;

    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> optional = JpaRepository.findByEmail(email);
        return optional.map(UserMapper::toDomain).orElse(null);
    }
}
