package com.ratovo.KitapoMbola.adapter.jpa.repository;

import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByUuid(String uuid);
}