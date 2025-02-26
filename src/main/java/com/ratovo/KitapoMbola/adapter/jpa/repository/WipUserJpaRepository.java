package com.ratovo.KitapoMbola.adapter.jpa.repository;

import com.ratovo.KitapoMbola.adapter.jpa.data.WipUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WipUserJpaRepository extends JpaRepository<WipUserEntity, Long> {
    Optional<WipUserEntity> findByEmail(String email);
}
