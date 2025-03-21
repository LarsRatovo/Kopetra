package com.ratovo.KitapoMbola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratovo.KitapoMbola.domain.User;
import com.ratovo.KitapoMbola.dto.request.WipUpsertRequestDto;
import com.ratovo.KitapoMbola.dto.response.UpsertWipResponseDto;
import com.ratovo.KitapoMbola.exception.*;
import com.ratovo.KitapoMbola.fixture.UserFixture;
import com.ratovo.KitapoMbola.fixture.WipFixture;
import com.ratovo.KitapoMbola.service.UserService;
import com.ratovo.KitapoMbola.useCase.UserUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

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
    void shouldCreateWipUser(){
        String uuid = UUID.randomUUID().toString();
        when(userUseCase.upsertWipUser(anyString(),any(WipUpsertRequestDto.class))).thenReturn(uuid);
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/users/wip/{email}", WipFixture.validWip.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(WipFixture.validWip))
        ).andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(new UpsertWipResponseDto(uuid))));
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
                .andExpect(jsonPath("messages").value(new EmailAlreadyUsedException().getDefaultMessage()));
    }

    @Test
    @SneakyThrows
    void shouldCreateAccount(){
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("wip=%s&password=%s".formatted("validUuid","abcdeF1;"))
                ).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldThrowInvalidWip(){
        doThrow(new InvalidWipException()).when(userUseCase).createAccount(anyString(),anyString());
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("wip=%s&password=%s".formatted("validUuid","abcdeF1;"))
        ).andExpect(status().is4xxClientError())
        .andExpect(jsonPath("messages").value(new InvalidWipException().getDefaultMessage()));
    }

    @Test
    @SneakyThrows
    void shouldInvalidPassword(){
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("wip=%s&password=%s".formatted("validUuid","invalidPassword"))
        ).andExpect(status().is(400))
        .andExpect(jsonPath("messages").value("Le mot de passe est invalide"));
    }

    @Test
    @SneakyThrows
    void shouldLogin(){
        when(this.userUseCase.login(anyString(),anyString())).thenReturn(UserFixture.tokenFixture);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("username=login@mail.com&password=password")
        ).andExpect(status().isOk())
        .andExpect(content().string(UserFixture.tokenFixture));
    }

    @Test
    @SneakyThrows
    void shouldThrowNoLoginFound(){
        when(this.userUseCase.login(anyString(),anyString())).thenThrow(new BadCredentialException());
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content("username=login@mail.com&password=password")
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("messages").value(new BadCredentialException().getDefaultMessage()));
    }

    @Test
    @SneakyThrows
    void shouldProceedToForgotPassword(){
        when(this.userUseCase.forgotPassword(UserFixture.user1.getEmail())).thenReturn(UserFixture.user1.getUuid());
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/users/{userName}/forgot-password",UserFixture.user1.getEmail())
        ).andExpect(status().isOk())
        .andExpect(jsonPath("uuid").value(UserFixture.user1.getUuid()));
    }

    @Test
    @SneakyThrows
    void shouldThrowNoUserFound(){
        when(this.userUseCase.forgotPassword(anyString())).thenThrow(new UserNotFoundException());
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/users/{userName}/forgot-password",UserFixture.user1.getEmail())
        ).andExpect(status().is4xxClientError())
        .andExpect(jsonPath("messages").value(new UserNotFoundException().getDefaultMessage()));
    }

    @Test
    @SneakyThrows
    void shouldUpdatePassword(){
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/users/{uuid}/reset-password",UserFixture.user1.getUuid())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("password=aBcdeF1;")
        ).andExpect(status().isOk());
        verify(this.userUseCase).resetPassword(eq(UserFixture.user1.getUuid()),eq("aBcdeF1;"));
    }
}
