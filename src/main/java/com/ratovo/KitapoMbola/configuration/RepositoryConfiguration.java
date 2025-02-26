package com.ratovo.KitapoMbola.configuration;

import com.ratovo.KitapoMbola.adapter.jpa.WipUserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.repository.WipUserJpaRepository;
import com.ratovo.KitapoMbola.port.WipUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public WipUserRepository getWipRepository(WipUserJpaRepository wipRepository) {
        return new WipUserDatabaseAdapter(wipRepository);
    }
}
