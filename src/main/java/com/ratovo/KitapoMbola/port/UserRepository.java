package com.ratovo.KitapoMbola.port;

import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;

public interface UserRepository {
    User findByUuid(String uuid);
    User findByEmail(String email);
    void createUser(User user);
    void saveLogin(Login login);
    String login(Login login);
}