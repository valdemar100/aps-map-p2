package com.aps.auth.service;

import com.aps.auth.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService("test-jwt-secret-key-com-minimo-32-chars", 3600000L);
    }

    @Test
    void gerarToken_deveGerarTokenValido() {
        Usuario usuario = new Usuario("aluno1", "hash", "João Silva");
        usuario.setId(42L);

        String token = tokenService.gerarToken(usuario);

        assertThat(token).isNotBlank();
        assertThat(tokenService.extrairIdUsuario(token)).isEqualTo(42L);
    }

    @Test
    void validarToken_deveLancarExcecaoParaTokenInvalido() {
        assertThatThrownBy(() -> tokenService.validarToken("token.invalido"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
