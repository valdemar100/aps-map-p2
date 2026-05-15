package com.aps.auth.controller;

import com.aps.auth.dto.CadastroRequest;
import com.aps.auth.dto.DadosLogin;
import com.aps.auth.dto.DadosUsuario;
import com.aps.auth.dto.TokenResponse;
import com.aps.auth.model.Usuario;
import com.aps.auth.service.TokenService;
import com.aps.auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AutenticacaoController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public AutenticacaoController(UsuarioService usuarioService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody DadosLogin dadosLogin) {
        if (!usuarioService.validarSenha(dadosLogin.login(), dadosLogin.senha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioService.buscarPorLogin(dadosLogin.login());
        if (!usuario.isAtivo()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String token = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(TokenResponse.bearer(token));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<DadosUsuario> cadastrar(@Valid @RequestBody CadastroRequest request) {
        Usuario usuario = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.toDadosUsuario(usuario));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<DadosUsuario> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}
