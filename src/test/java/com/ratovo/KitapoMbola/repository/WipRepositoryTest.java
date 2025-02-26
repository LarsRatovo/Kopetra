package com.ratovo.KitapoMbola.repository;

import com.ratovo.KitapoMbola.adapter.jpa.WipUserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.data.WipUserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.WipUserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.WipUserJpaRepository;
import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.fixture.WipFixture;
import com.ratovo.KitapoMbola.port.WipUserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WipRepositoryTest {

    @Autowired
    private WipUserJpaRepository jpaRepository;

    @Test
    @SneakyThrows
    void shouldUpsertWip(){
        WipUserRepository wipRepository = new WipUserDatabaseAdapter(jpaRepository);
        wipRepository.save(WipFixture.wip1);
        Optional<WipUserEntity> optional = jpaRepository.findByEmail(WipFixture.wip1.getEmail());
        assertTrue(optional.isPresent());
        assertEquals(WipFixture.wip1,WipUserMapper.toDomain(optional.get()));
    }

    @Test
    @SneakyThrows
    void shouldUpdateWip(){
        this.jpaRepository.save(WipUserMapper.toData(WipFixture.wip1));
        WipUserRepository wipRepository = new WipUserDatabaseAdapter(jpaRepository);
        WipUser updated = WipUser.builder()
                .uuid(WipFixture.wip1.getUuid())
                .email(WipFixture.wip1.getEmail())
                .firstName("updated first name")
                .lastName("updated last name")
                .build();
        wipRepository.save(updated);
        Optional<WipUserEntity> optional = jpaRepository.findByEmail(WipFixture.wip1.getEmail());
        assertTrue(optional.isPresent());
        assertEquals(updated,WipUserMapper.toDomain(optional.get()));
    }

    @Test
    @SneakyThrows
    void shouldFoundWipWithEmail(){
        this.jpaRepository.saveAll(WipFixture.wipUsers.stream().map(WipUserMapper::toData).toList());
        WipUserRepository wipRepository = new WipUserDatabaseAdapter(jpaRepository);
        WipUser foundWipUser = wipRepository.findByEmail(WipFixture.wip1.getEmail());
        assertEquals(WipFixture.wip1,foundWipUser);
    }

    @Test
    @SneakyThrows
    void shouldReturnNull(){
        WipUserRepository wipRepository = new WipUserDatabaseAdapter(jpaRepository);
        assertNull(wipRepository.findByEmail(WipFixture.wip1.getEmail()));
    }
}
