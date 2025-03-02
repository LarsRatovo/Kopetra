package com.ratovo.KitapoMbola.domain;

import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ValidationCode {
    private String code;
    @Setter
    private String targetUuid;
    private ValidationCodeType type;
    private Timestamp createdAt;
    @Setter
    private Timestamp validatedAt;

    public ValidationCode(ValidationCode domain) {
        code = domain.getCode();
        targetUuid = domain.getTargetUuid();
        type = domain.getType();
        createdAt = domain.getCreatedAt();
        validatedAt = domain.getValidatedAt();
    }
}
