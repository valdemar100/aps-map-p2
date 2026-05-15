package com.aps.academico.integration;

import com.aps.academico.client.UsuarioClient;
import com.aps.academico.dto.CursoRequest;
import com.aps.academico.dto.DadosUsuario;
import com.aps.academico.repository.CursoRepository;
import com.aps.academico.repository.MatriculaRepository;
import com.aps.academico.service.CursoService;
import com.aps.academico.support.JwtTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AcademicoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @MockBean
    private UsuarioClient usuarioClient;

    private Long cursoId;

    @BeforeEach
    void setUp() {
        matriculaRepository.deleteAll();
        cursoRepository.deleteAll();
        CursoRequest request = new CursoRequest("Programação Java", 80);
        cursoId = cursoService.cadastrar(request).getId();

        when(usuarioClient.buscarDadosPerfil(anyLong())).thenAnswer(inv -> {
            Long id = inv.getArgument(0);
            return new DadosUsuario(id, "aluno" + id, "Aluno " + id, true);
        });
    }

    @Test
    void listarCursos_deveRetornarLista() throws Exception {
        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Programação Java"));
    }

    @Test
    void crudCurso_deveFuncionar() throws Exception {
        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Redes\",\"cargaHoraria\":60}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Redes"));

        mockMvc.perform(put("/cursos/" + cursoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Java Avançado\",\"cargaHoraria\":90}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Java Avançado"));
    }

    @Test
    void matricula_deveExigirToken() throws Exception {
        mockMvc.perform(post("/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCurso\":" + cursoId + "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void matricula_deveCriarComTokenValido() throws Exception {
        String auth = JwtTestHelper.bearer(1L, "aluno1", "João");

        mockMvc.perform(post("/matriculas")
                        .header("Authorization", auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCurso\":" + cursoId + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCurso").value("Programação Java"));

        mockMvc.perform(get("/matriculas").header("Authorization", auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(1));
    }

    @Test
    void excluirCurso_deveRetornar204() throws Exception {
        Long id = cursoService.cadastrar(new CursoRequest("Temporário", 20)).getId();

        mockMvc.perform(delete("/cursos/" + id))
                .andExpect(status().isNoContent());
    }
}
