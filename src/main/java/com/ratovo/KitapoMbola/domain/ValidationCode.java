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
    private String targetUuid;
    private ValidationCodeType type;
    private Timestamp createdAt;
    @Setter
    private Timestamp validatedAt;
}
