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

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ValidationCodeEntity cv SET cv.valid = false WHERE cv.valid = true AND (cv.validatedAt IS NULL OR cv.checkedAt IS NULL ) AND cv.targetUuid = ?1")
    void invalidateCodeSentToTarget(String targetUuid);
}
