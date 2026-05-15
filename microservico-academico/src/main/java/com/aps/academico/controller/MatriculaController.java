package com.aps.academico.controller;

import com.aps.academico.dto.MatriculaRequest;
import com.aps.academico.dto.MatriculaResponse;
import com.aps.academico.service.MatriculaService;
import com.aps.academico.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final TokenService tokenService;

    public MatriculaController(MatriculaService matriculaService, TokenService tokenService) {
        this.matriculaService = matriculaService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<MatriculaResponse> realizarMatricula(
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody MatriculaRequest request) {
        Long idAluno = tokenService.extrairIdUsuario(authorization);
        MatriculaResponse response = matriculaService.matricularAluno(idAluno, request.idCurso());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponse>> listarMinhasMatriculas(
            @RequestHeader("Authorization") String authorization) {
        Long idAluno = tokenService.extrairIdUsuario(authorization);
        return ResponseEntity.ok(matriculaService.listarPorUsuario(idAluno));
    }
}
