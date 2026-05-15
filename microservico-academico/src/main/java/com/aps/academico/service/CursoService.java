package com.aps.academico.service;

import com.aps.academico.dto.CursoRequest;
import com.aps.academico.model.Curso;
import com.aps.academico.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));
    }

    public Curso cadastrar(CursoRequest request) {
        Curso curso = new Curso(request.nome(), request.cargaHoraria());
        return cursoRepository.save(curso);
    }

    public Curso atualizar(Long id, CursoRequest request) {
        Curso curso = buscarPorId(id);
        curso.setNome(request.nome());
        curso.setCargaHoraria(request.cargaHoraria());
        return cursoRepository.save(curso);
    }

    public void remover(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("Curso não encontrado");
        }
        cursoRepository.deleteById(id);
    }
}
