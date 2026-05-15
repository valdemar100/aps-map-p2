package com.aps.academico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CursoRequest(
        @NotBlank String nome,
        @NotNull @Positive Integer cargaHoraria
) {
}
