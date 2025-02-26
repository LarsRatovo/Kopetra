package com.ratovo.KitapoMbola.adapter.jpa.mapper;

import com.ratovo.KitapoMbola.adapter.jpa.data.UserEntity;
import com.ratovo.KitapoMbola.domain.User;

public class UserMapper {
    public static User toDomain(UserEntity data){
        return User.builder()
                .uuid(data.getUuid())
                .email(data.getEmail())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .build();
    }

    public static UserEntity toData(User user){
        return UserEntity.builder()
                .uuid(user.getUuid())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
