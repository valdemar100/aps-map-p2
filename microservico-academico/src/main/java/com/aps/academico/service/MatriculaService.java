package com.aps.academico.service;

import com.aps.academico.client.UsuarioClient;
import com.aps.academico.dto.DadosUsuario;
import com.aps.academico.dto.MatriculaResponse;
import com.aps.academico.model.Curso;
import com.aps.academico.model.Matricula;
import com.aps.academico.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final CursoService cursoService;
    private final UsuarioClient usuarioClient;

    public MatriculaService(
            MatriculaRepository matriculaRepository,
            CursoService cursoService,
            UsuarioClient usuarioClient) {
        this.matriculaRepository = matriculaRepository;
        this.cursoService = cursoService;
        this.usuarioClient = usuarioClient;
    }

    public MatriculaResponse matricularAluno(Long idUsuario, Long idCurso) {
        DadosUsuario usuario = usuarioClient.buscarDadosPerfil(idUsuario);
        validarRegrasMatricula(usuario, idUsuario, idCurso);

        Curso curso = cursoService.buscarPorId(idCurso);
        Matricula matricula = criarMatricula(idUsuario, idCurso);
        Matricula salva = matriculaRepository.save(matricula);

        return MatriculaResponse.of(salva, usuario.nome(), curso.getNome());
    }

    public List<MatriculaResponse> listarPorUsuario(Long idUsuario) {
        return matriculaRepository.findByIdUsuario(idUsuario).stream()
                .map(matricula -> {
                    DadosUsuario usuario = usuarioClient.buscarDadosPerfil(matricula.getIdUsuario());
                    Curso curso = cursoService.buscarPorId(matricula.getIdCurso());
                    return MatriculaResponse.of(matricula, usuario.nome(), curso.getNome());
                })
                .toList();
    }

    private void validarRegrasMatricula(DadosUsuario usuario, Long idUsuario, Long idCurso) {
        if (!usuario.ativo()) {
            throw new IllegalArgumentException("Usuário inativo não pode se matricular");
        }
        cursoService.buscarPorId(idCurso);
        if (matriculaRepository.existsByIdUsuarioAndIdCurso(idUsuario, idCurso)) {
            throw new IllegalArgumentException("Aluno já matriculado neste curso");
        }
    }

    private Matricula criarMatricula(Long idUsuario, Long idCurso) {
        return new Matricula(idUsuario, idCurso);
    }
}
