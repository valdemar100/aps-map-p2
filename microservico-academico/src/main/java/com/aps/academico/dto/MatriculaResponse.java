package com.aps.academico.dto;

import com.aps.academico.model.Matricula;

import java.time.LocalDate;

public record MatriculaResponse(
        Long id,
        LocalDate data,
        Long idUsuario,
        Long idCurso,
        String nomeAluno,
        String nomeCurso
) {
    public static MatriculaResponse of(Matricula matricula, String nomeAluno, String nomeCurso) {
        return new MatriculaResponse(
                matricula.getId(),
                matricula.getData(),
                matricula.getIdUsuario(),
                matricula.getIdCurso(),
                nomeAluno,
                nomeCurso
        );
    }
}
