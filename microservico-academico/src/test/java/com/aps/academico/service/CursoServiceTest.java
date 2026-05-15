package com.aps.academico.service;

import com.aps.academico.dto.CursoRequest;
import com.aps.academico.model.Curso;
import com.aps.academico.repository.CursoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @Test
    void cadastrar_deveSalvarCurso() {
        CursoRequest request = new CursoRequest("Java", 80);
        when(cursoRepository.save(any(Curso.class))).thenAnswer(inv -> {
            Curso c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        Curso salvo = cursoService.cadastrar(request);

        assertThat(salvo.getNome()).isEqualTo("Java");
        assertThat(salvo.getCargaHoraria()).isEqualTo(80);
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void buscarPorId_deveLancarQuandoNaoEncontrado() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursoService.buscarPorId(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Curso não encontrado");
    }

    @Test
    void remover_deveExcluirCursoExistente() {
        when(cursoRepository.existsById(1L)).thenReturn(true);

        cursoService.remover(1L);

        verify(cursoRepository).deleteById(1L);
    }
}
