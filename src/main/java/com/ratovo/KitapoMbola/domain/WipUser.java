package com.ratovo.KitapoMbola.domain;

import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    public WipUser(WipUpsertRequestDto dto) {
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
    }
}
