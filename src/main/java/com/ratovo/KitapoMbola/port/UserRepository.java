package com.ratovo.KitapoMbola.port;

import com.ratovo.KitapoMbola.domain.Login;
import com.ratovo.KitapoMbola.domain.User;

public interface UserRepository {
    User findByEmail(String email);
    void createUser(User user);
    void createLogin(Login login);

    String login(Login login);
}