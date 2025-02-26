package com.ratovo.KitapoMbola.port;

import com.ratovo.KitapoMbola.domain.User;

public interface UserRepository {
    User findByEmail(String email);
}