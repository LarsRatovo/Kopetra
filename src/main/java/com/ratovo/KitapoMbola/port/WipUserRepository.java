package com.ratovo.KitapoMbola.port;

import com.ratovo.KitapoMbola.domain.WipUser;

public interface WipUserRepository {
    WipUser findByEmail(String email);
    void save(WipUser wipUser);
    WipUser findByUuid(String uuid);
    void removeWipAccount(String uuid);
}
