package com.ratovo.KitapoMbola.repository;

import com.ratovo.KitapoMbola.adapter.jpa.UserDatabaseAdapter;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.UserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.UserJpaRepository;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.fixture.UserFixture;
import com.ratovo.KitapoMbola.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserJpaRepository jpaRepository;

    @Test
    void shouldReturnExistingUserWithEmail(){
        this.jpaRepository.save(UserMapper.toData(UserFixture.user1));
        UserRepository userRepository = new UserDatabaseAdapter(jpaRepository);
        User foundUser = userRepository.findByEmail(UserFixture.user1.getEmail());
        assertEquals(UserFixture.user1,foundUser);
    }
}
