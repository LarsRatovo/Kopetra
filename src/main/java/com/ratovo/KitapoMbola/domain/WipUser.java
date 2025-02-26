package com.ratovo.KitapoMbola.domain;

import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WipUser {
    private String uuid;
    private String email;
    private String firstName;
    private String lastName;

    public WipUser(WipUpsertRequestDto dto) {
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
    }
}
