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
    static {
        ok = ValidationCode.builder()
                .code("3679")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .valid(false)
                .build();

        code1 = ValidationCode.builder()
                .code("12342")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .checkedAt(Timestamp.from(Instant.now()))
                .valid(true)
                .build();

        code2 = ValidationCode.builder()
                .code("12Af2")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .validatedAt(Timestamp.from(Instant.now()))
                .valid(true)
                .build();

        code3 = ValidationCode.builder()
                .code("1G34F")
                .type(ValidationCodeType.REGISTRATION)
                .targetUuid(validTargetUuid)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        codes = Arrays.asList(code1, code2, code3);

    }
}
