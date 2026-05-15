package com.aps.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosLogin(
        @NotBlank String login,
        @NotBlank String senha
) {
}
