package com.aps.academico.repository;

import com.aps.academico.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    boolean existsByIdUsuarioAndIdCurso(Long idUsuario, Long idCurso);

    List<Matricula> findByIdUsuario(Long idUsuario);
}
