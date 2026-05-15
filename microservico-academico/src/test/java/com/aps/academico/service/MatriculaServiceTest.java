package com.aps.academico.service;

import com.aps.academico.client.UsuarioClient;
import com.aps.academico.dto.DadosUsuario;
import com.aps.academico.dto.MatriculaResponse;
import com.aps.academico.model.Curso;
import com.aps.academico.model.Matricula;
import com.aps.academico.repository.MatriculaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private CursoService cursoService;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private MatriculaService matriculaService;

    @Test
    void matricularAluno_deveCriarMatriculaComSucesso() {
        DadosUsuario usuario = new DadosUsuario(1L, "aluno1", "João", true);
        Curso curso = new Curso("Java", 80);
        curso.setId(10L);
        Matricula matricula = new Matricula(1L, 10L);
        matricula.setId(100L);

        when(usuarioClient.buscarDadosPerfil(1L)).thenReturn(usuario);
        when(cursoService.buscarPorId(10L)).thenReturn(curso);
        when(matriculaRepository.existsByIdUsuarioAndIdCurso(1L, 10L)).thenReturn(false);
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        MatriculaResponse response = matriculaService.matricularAluno(1L, 10L);

        assertThat(response.nomeAluno()).isEqualTo("João");
        assertThat(response.nomeCurso()).isEqualTo("Java");
        verify(matriculaRepository).save(any(Matricula.class));
    }

    @Test
    void matricularAluno_deveRejeitarUsuarioInativo() {
        DadosUsuario inativo = new DadosUsuario(1L, "aluno1", "João", false);
        when(usuarioClient.buscarDadosPerfil(1L)).thenReturn(inativo);

        assertThatThrownBy(() -> matriculaService.matricularAluno(1L, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inativo");

        verify(matriculaRepository, never()).save(any());
    }

    @Test
    void matricularAluno_deveRejeitarMatriculaDuplicada() {
        DadosUsuario usuario = new DadosUsuario(1L, "aluno1", "João", true);
        when(usuarioClient.buscarDadosPerfil(1L)).thenReturn(usuario);
        when(cursoService.buscarPorId(10L)).thenReturn(new Curso("Java", 80));
        when(matriculaRepository.existsByIdUsuarioAndIdCurso(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> matriculaService.matricularAluno(1L, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("já matriculado");
    }
}
