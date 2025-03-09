package com.ratovo.KitapoMbola.fixture;


import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;

import java.util.UUID;

public class UserFixture {
    public static User user1;
    public static Login login1;
    public static String tokenFixture = "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6Ijg5NDlhNDE1N2ZkMDZlNDEyYTA3ODIzNTNkNDk1MGJhIn0.eyJ1dWlkIjoiZWJhMTYyOTgtMzVjNi00NzU3LWI5YTktZGVjODQ4NzZkYjdkIiwidXNlcm5hbWUiOiJ1c2VyQG1haWwuY29tIiwiZmlyc3ROYW1lIjoiam9obiIsImxhc3ROYW1lIjoiRG9lIn0.fafBwCa1zp01he_bEFWDfj3RWv2sCiblGVyoyIh9GpOI94JX3bKiAC4jDgdoJoJ888zz6YYn-Vlt1mZ5uV3k5Q";
    static {
        user1 = User.builder()
                .uuid("eba16298-35c6-4757-b9a9-dec84876db7d")
                .email("user@mail.com")
                .firstName("John")
                .lastName("Doe")
                .build();
        login1 = Login.builder()
                .email("user@mail.com")
                .password("password")
                .build();
    }
}
