package com.ratovo.KitapoMbola.adapter.jpa.mapper;

import com.ratovo.KitapoMbola.adapter.jpa.data.WipUserEntity;
import com.ratovo.KitapoMbola.domain.WipUser;

public class WipUserMapper {
    public static WipUser toDomain(WipUserEntity data){
        return WipUser.builder()
                .uuid(data.getUuid())
                .email(data.getEmail())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .build();
    }

    public static WipUserEntity toData(WipUser data){
        return WipUserEntity.builder()
                .email(data.getEmail())
                .uuid(data.getUuid())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .build();
    }
}
