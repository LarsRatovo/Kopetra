package com.ratovo.KitapoMbola.adapter.jpa.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "validation_code")
public class ValidationCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String targetUuid;
    private String type;
    private Timestamp createdAt;
    private Timestamp validatedAt;
    private Timestamp checkedAt;
    private Boolean valid;
}
