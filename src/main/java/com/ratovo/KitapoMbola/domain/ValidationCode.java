package com.ratovo.KitapoMbola.domain;

import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@EqualsAndHashCode
public class ValidationCode {
    private String code;
    @Setter
    private String targetUuid;
    private ValidationCodeType type;
    private Timestamp createdAt;
    @Setter
    private Timestamp validatedAt;
    @Setter
    private Timestamp checkedAt;
    @Builder.Default
    @Setter
    private Boolean valid = true;
}
