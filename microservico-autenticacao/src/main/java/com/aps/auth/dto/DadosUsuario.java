package com.aps.auth.dto;

public record DadosUsuario(
        Long id,
        String login,
        String nome,
        boolean ativo
) {
}
