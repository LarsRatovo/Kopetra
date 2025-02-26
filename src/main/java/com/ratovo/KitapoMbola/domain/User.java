package com.ratovo.KitapoMbola.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String email;
    private String uuid;
    private String firstName;
    private String lastName;
}
