package com.ratovo.KitapoMbola.adapter.jpa;

import com.ratovo.KitapoMbola.adapter.jpa.data.WipUserEntity;
import com.ratovo.KitapoMbola.adapter.jpa.mapper.WipUserMapper;
import com.ratovo.KitapoMbola.adapter.jpa.repository.WipUserJpaRepository;
import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.port.WipUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class WipUserDatabaseAdapter implements WipUserRepository {

    private final WipUserJpaRepository jpaRepository;

    @Override
    public WipUser findByEmail(String email) {
        Optional<WipUserEntity> optional = jpaRepository.findByEmail(email);
        return optional.map(WipUserMapper::toDomain).orElse(null);
    }

    @Override
    public void save(WipUser wipUser) {
        if(wipUser == null) return;
        WipUserEntity data = WipUserMapper.toData(wipUser);
        Optional<WipUserEntity> existingData = jpaRepository.findByEmail(wipUser.getEmail());
        existingData.ifPresent(wipUserEntity -> data.setId(wipUserEntity.getId()));
        this.jpaRepository.save(data);
    }

    @Override
    public WipUser findByUuid(String uuid) {
        Optional<WipUserEntity> optional = jpaRepository.findByUuid(uuid);
        return optional.map(WipUserMapper::toDomain).orElse(null);
    }

    @Override
    public void removeWipAccount(String uuid) {
        Optional<WipUserEntity> optional = jpaRepository.findByUuid(uuid);
        optional.ifPresent(this.jpaRepository::delete);
    }
}
