package com.aps.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastroRequest(
        @NotBlank String login,
        @NotBlank String senha,
        @NotBlank String nome
) {
}
