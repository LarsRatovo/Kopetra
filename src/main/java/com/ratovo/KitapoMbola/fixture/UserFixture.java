package com.ratovo.KitapoMbola.fixture;


import com.ratovo.KitapoMbola.domain.User;

import java.util.UUID;

public class UserFixture {
    public static User user1;
    static {
        user1 = User.builder()
                .uuid(UUID.randomUUID().toString())
                .email("user@mail.com")
                .firstName("John")
                .lastName("Doe")
                .build();
    }
}
