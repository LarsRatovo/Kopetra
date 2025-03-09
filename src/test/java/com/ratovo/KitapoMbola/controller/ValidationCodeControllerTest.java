package com.ratovo.KitapoMbola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratovo.KitapoMbola.dto.response.UpsertWipResponseDto;
import com.ratovo.KitapoMbola.exception.InvalidCodeException;
import com.ratovo.KitapoMbola.fixture.ValidationCodeFixture;
import com.ratovo.KitapoMbola.fixture.WipFixture;
import com.ratovo.KitapoMbola.service.ValidationCodeService;
import com.ratovo.KitapoMbola.useCase.ValidationCodeUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ValidationCodeController.class)
@Import({ValidationCodeService.class})
public class ValidationCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ValidationCodeUseCase useCase;

    @Test
    @SneakyThrows
    void shouldValidateCode(){
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/validation-code/validate")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content("targetUuid=%s&code=%s".formatted(ValidationCodeFixture.validTargetUuid,ValidationCodeFixture.ok.getCode()))
                ).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void shouldReturnInvalidCode(){
        doThrow(new InvalidCodeException()).when(useCase).validateCode(anyString(),anyString());
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/validation-code/validate",WipFixture.wip1.getUuid())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content("targetUuid=%s&code=invalid".formatted(ValidationCodeFixture.validTargetUuid))
                ).andExpect(status().is(400))
                .andExpect(jsonPath("messages").value(new InvalidCodeException().getDefaultMessage()));
    }
}
