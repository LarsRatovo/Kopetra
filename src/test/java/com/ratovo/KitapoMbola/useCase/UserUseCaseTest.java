package com.ratovo.KitapoMbola.useCase;

import com.ratovo.KitapoMbola.domain.WipUser;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.fixture.WipFixture;
import com.ratovo.KitapoMbola.port.UserRepository;
import com.ratovo.KitapoMbola.port.WipUserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserUseCaseTest {
    private final WipUserRepository wipRepository;
    private final UserRepository userRepository;
    private final UserUseCase useCase;

    public UserUseCaseTest(){
        this.wipRepository = mock(WipUserRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.useCase = new UserUseCase(userRepository,wipRepository);
    }

    @Test
    @SneakyThrows
    void shouldCreateWipUser(){
        when(wipRepository.findByEmail(anyString())).thenReturn(null);
        doNothing().when(wipRepository).save(any(WipUser.class));
        this.useCase.upsertWipUser(WipFixture.validWip.getEmail(),WipFixture.validWip);

        verify(userRepository).findByEmail(anyString());
        verify(wipRepository).findByEmail(anyString());
        verify(wipRepository).save(argThat(wip -> wip.getUuid() != null));
    }

    @Test
    @SneakyThrows
    void shouldUpdateExistingWipUser(){
        when(wipRepository.findByEmail(anyString())).thenReturn(WipFixture.wip1);
        doNothing().when(wipRepository).save(any(WipUser.class));
        WipUpsertRequestDto updateWip =  WipUpsertRequestDto
                .builder()
                .email(WipFixture.wip1.getEmail())
                .lastName("Last name updated")
                .firstName("First name updated")
                .build();
        this.useCase.upsertWipUser(WipFixture.validWip.getEmail(), updateWip);

        verify(wipRepository).findByEmail(anyString());
        verify(wipRepository).save(argThat(wip ->
                Objects.equals(wip.getUuid(), WipFixture.wip1.getUuid()) &&
                Objects.equals(wip.getLastName(), WipFixture.wip1.getLastName()) &&
                Objects.equals(wip.getFirstName(), WipFixture.wip1.getFirstName())
        ));
    }
}
