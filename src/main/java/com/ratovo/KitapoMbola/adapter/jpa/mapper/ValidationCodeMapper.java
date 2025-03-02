package com.ratovo.KitapoMbola.adapter.jpa.mapper;

import com.ratovo.KitapoMbola.adapter.jpa.data.ValidationCodeEntity;
import com.ratovo.KitapoMbola.domain.ValidationCode;
import com.ratovo.KitapoMbola.enumeration.ValidationCodeType;

public class ValidationCodeMapper {
    public static ValidationCode toDomain(ValidationCodeEntity data){
        if(data == null) return null;
        return ValidationCode.builder()
                .code(data.getCode())
                .targetUuid(data.getTargetUuid())
                .type(ValidationCodeType.valueOf(data.getType()))
                .validatedAt(data.getValidatedAt())
                .createdAt(data.getCreatedAt())
                .build();
    }

    public static ValidationCodeEntity toData(ValidationCode domain){
        if(domain == null) return null;
        return ValidationCodeEntity.builder()
                .code(domain.getCode())
                .targetUuid(domain.getTargetUuid())
                .type(domain.getType().name())
                .createdAt(domain.getCreatedAt())
                .validatedAt(domain.getValidatedAt())
                .build();
    }
}
