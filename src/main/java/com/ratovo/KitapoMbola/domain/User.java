package com.ratovo.KitapoMbola.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    @NotNull
    private String email;
    @NotNull
    private String uuid;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
