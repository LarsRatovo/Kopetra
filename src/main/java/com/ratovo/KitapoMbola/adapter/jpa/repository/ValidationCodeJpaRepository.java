package com.ratovo.KitapoMbola.adapter.jpa.repository;

import com.ratovo.KitapoMbola.adapter.jpa.data.ValidationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ValidationCodeJpaRepository extends JpaRepository<ValidationCodeEntity, Long> {
    Optional<ValidationCodeEntity> findByCode(String code);
    Optional<ValidationCodeEntity> findByCodeAndTargetUuid(String code, String targetUuid);
    List<ValidationCodeEntity> findByTargetUuid(String targetUuid);
    void deleteAllByTargetUuid(String targetUuid);
    Optional<ValidationCodeEntity> findByTargetUuidAndValidatedAtIsNotNull(String targetUuid);
}
