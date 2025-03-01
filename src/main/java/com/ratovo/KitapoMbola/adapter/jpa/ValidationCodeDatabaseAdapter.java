package com.ratovo.KitapoMbola.adapter.jpa;

import com.ratovo.KitapoMbola.adapter.jpa.data.ValidationCodeEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.ValidationCodeMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.ValidationCodeJpaRepository;
import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import com.ratovo.KitapoMbola.port.ValidationCodeRepository;
import lombok.RequiredArgsConstructor;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class ValidationCodeDatabaseAdapter implements ValidationCodeRepository {

    private final ValidationCodeJpaRepository jpaRepository;

    @Override
    public ValidationCode generateCode(ValidationCodeType type) {
        try {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            String secretKey = "lkegitp==";
            String code = ValidationCodeRepository.generateCode(secretKey,timestamp.getTime());
            return ValidationCode.builder()
                    .code(code)
                    .type(type)
                    .createdAt(timestamp)
                    .build();
        }catch (NoSuchAlgorithmException| InvalidKeyException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void save(ValidationCode code) {
        this.jpaRepository.save(ValidationCodeMapper.toData(code));
    }

    @Override
    public void invalidateCodeSentTo(String targetUuid) {
        this.jpaRepository.invalidateCodeSentToTarget(targetUuid);
    }

    @Override
    public ValidationCode findByCode(String code, String targetUuid) {
        Optional<ValidationCodeEntity> optional = this.jpaRepository.findByCodeAndTargetUuid(code, targetUuid);
        return optional.map(ValidationCodeMapper::toDomain).orElse(null);
    }
}
