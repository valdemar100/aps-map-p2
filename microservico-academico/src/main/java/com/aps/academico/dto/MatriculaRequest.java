package com.aps.academico.dto;

import jakarta.validation.constraints.NotNull;

public record MatriculaRequest(
        @NotNull Long idCurso
) {
}
