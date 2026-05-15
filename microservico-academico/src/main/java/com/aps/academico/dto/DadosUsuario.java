package com.aps.academico.dto;

public record DadosUsuario(
        Long id,
        String login,
        String nome,
        boolean ativo
) {
}
