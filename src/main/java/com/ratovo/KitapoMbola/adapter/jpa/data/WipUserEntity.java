package com.ratovo.KitapoMbola.adapter.jpa.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wip")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WipUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String uuid;
    private String firstName;
    private String lastName;
}
