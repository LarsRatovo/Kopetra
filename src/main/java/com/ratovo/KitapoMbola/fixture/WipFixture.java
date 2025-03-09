package com.ratovo.KitapoMbola.fixture;

import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WipFixture {
    public static WipUpsertRequestDto validWip;
    public static WipUpsertRequestDto invalidWip;
    public static WipUpsertRequestDto invalidWip2;
    public static WipUser wip1;
    public static WipUser wip2;
    public static List<WipUser> wipUsers;
    static {
        validWip = WipUpsertRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@valid.com")
                .build();

        invalidWip = WipUpsertRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("invalidEmail")
                .build();

        invalidWip2 = WipUpsertRequestDto.builder()
                .email("invalidEmail")
                .build();

        wip1 = WipUser.builder()
                .uuid(UUID.randomUUID().toString())
                .email("1@wip.com")
                .firstName("first")
                .lastName("wip")
                .build();

        wip2 = WipUser.builder()
                .uuid(UUID.randomUUID().toString())
                .build();
        wipUsers = Arrays.asList(wip1, wip2);
    }
}
