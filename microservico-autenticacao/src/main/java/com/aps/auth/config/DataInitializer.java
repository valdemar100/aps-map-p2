package com.aps.auth.config;

import com.aps.auth.dto.CadastroRequest;
import com.aps.auth.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioService usuarioService;

    public DataInitializer(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) {
        try {
            usuarioService.cadastrar(new CadastroRequest("admin", "admin123", "Administrador"));
        } catch (IllegalArgumentException ignored) {
        }
        try {
            usuarioService.cadastrar(new CadastroRequest("aluno1", "aluno123", "João Silva"));
        } catch (IllegalArgumentException ignored) {
        }
    }
}
