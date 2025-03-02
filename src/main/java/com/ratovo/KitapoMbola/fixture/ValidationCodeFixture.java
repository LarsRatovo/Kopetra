package com.ratovo.KitapoMbola.fixture;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ValidationCodeFixture {
    public static ValidationCode ok;
    public static ValidationCode code1;
    public static ValidationCode code2;
    public static ValidationCode code3;
    public static String validTargetUuid = UUID.randomUUID().toString();
    public static List<ValidationCode> codes;
    public static ValidationCode differentTargetUuid;
    static {
        ok = ValidationCode.builder()
                .code("3679")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        code1 = ValidationCode.builder()
                .code("12342")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        code2 = ValidationCode.builder()
                .code("12Af2")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .validatedAt(Timestamp.from(Instant.now().plusSeconds(120)))
                .build();

        code3 = ValidationCode.builder()
                .code("1G34F")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        differentTargetUuid = ValidationCode.builder()
                .code("ZJE89")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(UUID.randomUUID().toString())
                .createdAt(Timestamp.from(Instant.now()))
                .validatedAt(Timestamp.from(Instant.now()))
                .build();
        codes = Arrays.asList(code1, code2, code3, differentTargetUuid);

    }
}
