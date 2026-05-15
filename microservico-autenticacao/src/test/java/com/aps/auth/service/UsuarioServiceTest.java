package com.aps.auth.service;

import com.aps.auth.dto.CadastroRequest;
import com.aps.auth.model.Usuario;
import com.aps.auth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioSalvo;

    @BeforeEach
    void setUp() {
        usuarioSalvo = new Usuario("aluno1", new BCryptPasswordEncoder().encode("aluno123"), "João");
        usuarioSalvo.setId(1L);
    }

    @Test
    void validarSenha_deveRetornarTrueQuandoSenhaCorreta() {
        when(usuarioRepository.findByLogin("aluno1")).thenReturn(Optional.of(usuarioSalvo));

        assertThat(usuarioService.validarSenha("aluno1", "aluno123")).isTrue();
    }

    @Test
    void validarSenha_deveRetornarFalseQuandoSenhaIncorreta() {
        when(usuarioRepository.findByLogin("aluno1")).thenReturn(Optional.of(usuarioSalvo));

        assertThat(usuarioService.validarSenha("aluno1", "errada")).isFalse();
    }

    @Test
    void cadastrar_devePersistirUsuarioComSenhaCriptografada() {
        when(usuarioRepository.existsByLogin("novo")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(2L);
            return u;
        });

        Usuario criado = usuarioService.cadastrar(new CadastroRequest("novo", "senha123", "Novo Aluno"));

        assertThat(criado.getLogin()).isEqualTo("novo");
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isNotEqualTo("senha123");
    }

    @Test
    void cadastrar_deveLancarExcecaoQuandoLoginDuplicado() {
        when(usuarioRepository.existsByLogin("aluno1")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.cadastrar(new CadastroRequest("aluno1", "x", "X")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Login já cadastrado");
    }

    @Test
    void buscarPorId_deveRetornarDadosUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioSalvo));

        assertThat(usuarioService.buscarPorId(1L).login()).isEqualTo("aluno1");
    }
}
