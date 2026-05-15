package com.aps.auth.integration;

import com.aps.auth.dto.CadastroRequest;
import com.aps.auth.repository.UsuarioRepository;
import com.aps.auth.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AutenticacaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        usuarioService.cadastrar(new CadastroRequest("aluno1", "aluno123", "João Silva"));
    }

    @Test
    void login_deveRetornarTokenComCredenciaisValidas() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"aluno1\",\"senha\":\"aluno123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    void login_deveRetornar401ComSenhaInvalida() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"aluno1\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cadastroEBuscarUsuario_deveFuncionar() throws Exception {
        String body = mockMvc.perform(post("/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"maria\",\"senha\":\"senha123\",\"nome\":\"Maria\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("maria"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(body)
                .get("id")
                .asLong();

        mockMvc.perform(get("/usuarios/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }
}
