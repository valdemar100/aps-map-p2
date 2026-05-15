package com.aps.academico.client;

import com.aps.academico.dto.DadosUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClient {

    private final RestTemplate restTemplate;
    private final String authBaseUrl;

    public UsuarioClient(RestTemplate restTemplate, @Value("${auth.service.url}") String authBaseUrl) {
        this.restTemplate = restTemplate;
        this.authBaseUrl = authBaseUrl;
    }

    public DadosUsuario buscarDadosPerfil(Long idUsuario) {
        try {
            return restTemplate.getForObject(
                    authBaseUrl + "/usuarios/{id}",
                    DadosUsuario.class,
                    idUsuario
            );
        } catch (HttpClientErrorException.NotFound ex) {
            throw new IllegalArgumentException("Usuário não encontrado no serviço de autenticação");
        } catch (Exception ex) {
            throw new IllegalStateException("Falha na comunicação com o microsserviço de autenticação");
        }
    }
}
