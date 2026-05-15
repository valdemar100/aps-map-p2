package com.aps.academico.config;

import com.aps.academico.dto.CursoRequest;
import com.aps.academico.service.CursoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final CursoService cursoService;

    public DataInitializer(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @Override
    public void run(String... args) {
        if (cursoService.listarCursos().isEmpty()) {
            cursoService.cadastrar(new CursoRequest("Programação Java", 80));
            cursoService.cadastrar(new CursoRequest("Banco de Dados", 60));
            cursoService.cadastrar(new CursoRequest("Arquitetura de Software", 40));
        }
    }
}
