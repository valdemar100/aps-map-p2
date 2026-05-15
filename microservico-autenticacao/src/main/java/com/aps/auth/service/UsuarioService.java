package com.aps.auth.service;

import com.aps.auth.dto.CadastroRequest;
import com.aps.auth.dto.DadosUsuario;
import com.aps.auth.model.Usuario;
import com.aps.auth.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean validarSenha(String login, String senha) {
        return usuarioRepository.findByLogin(login)
                .map(usuario -> passwordEncoder.matches(senha, usuario.getSenha()))
                .orElse(false);
    }

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public DadosUsuario buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toDadosUsuario(usuario);
    }

    public Usuario cadastrar(CadastroRequest request) {
        if (usuarioRepository.existsByLogin(request.login())) {
            throw new IllegalArgumentException("Login já cadastrado");
        }

        Usuario usuario = new Usuario(
                request.login(),
                passwordEncoder.encode(request.senha()),
                request.nome()
        );
        return usuarioRepository.save(usuario);
    }

    public DadosUsuario toDadosUsuario(Usuario usuario) {
        return new DadosUsuario(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getNome(),
                usuario.isAtivo()
        );
    }
}
