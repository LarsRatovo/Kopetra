package com.ratovo.KitapoMbola.repository;

import com.ratovo.KitapoMbola.adapter.jpa.ValidationCodeDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.ValidationCodeMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.ValidationCodeJpaRepository;
import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import com.ratovo.KitapoMbola.fixture.ValidationCodeFixture;
import com.ratovo.KitapoMbola.port.ValidationCodeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ValidationCodeRepositoryTest {

    @Autowired
    private ValidationCodeJpaRepository jpaRepository;

    @Test
    @SneakyThrows
    void shouldGenerateValidationCode(){
        ValidationCodeRepository repository = new ValidationCodeDatabaseAdapter(jpaRepository);
        ValidationCode generatedValidationCode = repository.generateCode(ValidationCodeType.REGISTRATION,ValidationCodeFixture.validTargetUuid);
        assertTrue(
                generatedValidationCode.getCode() != null &&
                generatedValidationCode.getCreatedAt() != null &&
                generatedValidationCode.getValidatedAt() == null
        );
    }

    @AfterEach
    void cleanDatabase(){
        this.jpaRepository.deleteAll();
        this.jpaRepository.flush();
    }

    @Test
    void shouldSaveValidationCode(){
        ValidationCodeRepository repository = new ValidationCodeDatabaseAdapter(jpaRepository);
        repository.save(ValidationCodeFixture.ok);
        assertEquals(ValidationCodeFixture.ok, ValidationCodeMapper.toDomain(this.jpaRepository.findByCode(ValidationCodeFixture.ok.getCode()).orElse(null)));
    }

    @Test
    void shouldInvalidateCodeSentToTarget(){
        this.jpaRepository.saveAll(ValidationCodeFixture.codes.stream().map(ValidationCodeMapper::toData).toList());
        ValidationCodeRepository repository = new ValidationCodeDatabaseAdapter(jpaRepository);
        repository.invalidateCodeSentTo(ValidationCodeFixture.validTargetUuid);
        assertTrue(this.jpaRepository.findByTargetUuid(ValidationCodeFixture.validTargetUuid).isEmpty());
    }

    @Test
    void shouldFindCodeByCodeAndTargetUuid(){
        this.jpaRepository.saveAll(ValidationCodeFixture.codes.stream().map(ValidationCodeMapper::toData).toList());
        ValidationCodeRepository repository = new ValidationCodeDatabaseAdapter(jpaRepository);
        assertEquals(ValidationCodeFixture.code1,repository.findByCode(ValidationCodeFixture.code1.getCode(),ValidationCodeFixture.code1.getTargetUuid()));
    }

    @Test
    void shouldFindValidatedCodeByCodeAndTargetUuid(){
        this.jpaRepository.saveAll(ValidationCodeFixture.codes.stream().map(ValidationCodeMapper::toData).toList());
        ValidationCodeRepository repository = new ValidationCodeDatabaseAdapter(jpaRepository);
        assertEquals(
                ValidationCodeFixture.code2,
                repository.findValidatedCodeByTargetUuid(ValidationCodeFixture.code2.getTargetUuid())
        );
    }

    @Test
    void shouldOnlyFindValidatedCodeByCodeAndTargetUuid(){
        this.jpaRepository.saveAll(List.of(ValidationCodeMapper.toData(ValidationCodeFixture.code3)));
        ValidationCodeRepository repository = new ValidationCodeDatabaseAdapter(jpaRepository);
        assertNull(repository.findValidatedCodeByTargetUuid(ValidationCodeFixture.code3.getTargetUuid()));
    }

}
