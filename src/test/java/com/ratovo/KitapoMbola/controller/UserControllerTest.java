package com.ratovo.KitapoMbola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.exception.EmailAlreadyUsedException;
import com.ratovo.KitapoMbola.fixture.WipFixture;
import com.ratovo.KitapoMbola.service.UserService;
import com.ratovo.KitapoMbola.useCase.UserUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({UserService.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserUseCase userUseCase;

    @Test
    @SneakyThrows
    void shouldReturnNoBody(){
        doNothing().when(userUseCase).upsertWipUser(anyString(),any(WipUpsertRequestDto.class));
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/users/wip/{email}", WipFixture.validWip.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WipFixture.validWip))
        ).andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    @SneakyThrows
    void shouldReturnInvalidEmail(){
        String expectedMessage =  "L'email est invalide";
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/users/wip/{email}",WipFixture.validWip.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WipFixture.invalidWip))
        ).andExpect(status().is(400))
                .andExpect(jsonPath("messages").value(expectedMessage));
    }

    @Test
    @SneakyThrows
    void shouldReturnEmailAlreadyUsed(){
        doThrow(new EmailAlreadyUsedException()).when(userUseCase).upsertWipUser(anyString(),isNotNull(WipUpsertRequestDto.class));
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/users/wip/{email}",WipFixture.validWip.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WipFixture.validWip))
        ).andExpect(status().is(400))
                .andExpect(jsonPath("messages").value(EmailAlreadyUsedException.defaultMessage));
    }
}
