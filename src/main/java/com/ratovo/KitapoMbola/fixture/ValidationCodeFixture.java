package com.ratovo.KitapoMbola.fixture;

import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ValidationCodeFixture {
    public static ValidationCode ok;
    public static ValidationCode code1;
    public static ValidationCode code2;
    public static ValidationCode code3;
    public static String validTargetUuid = UserFixture.user1.getUuid();
    public static List<ValidationCode> codes;
    public static ValidationCode differentTargetUuid;
    public static ValidationCode passwordResetCode;
    public static ValidationCode checkedPasswordResetCode;

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
        passwordResetCode = ValidationCode.builder()
                .code("AB87KL")
                .type(ValidationCodeType.PASSWORD_RESET)
                .targetUuid(UserFixture.user1.getUuid())
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        checkedPasswordResetCode = ValidationCode.builder()
                .code("A187KL")
                .type(ValidationCodeType.PASSWORD_RESET)
                .targetUuid(UserFixture.user1.getUuid())
                .createdAt(Timestamp.from(Instant.now()))
                .validatedAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(30)))
                .build();
        codes = Arrays.asList(code1, code2, code3, differentTargetUuid,passwordResetCode);
    }
}
