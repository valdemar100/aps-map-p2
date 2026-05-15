package com.aps.academico.support;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

public final class JwtTestHelper {

    public static final String SECRET = "test-jwt-secret-key-com-minimo-32-chars";

    private JwtTestHelper() {
    }

    public static String tokenParaUsuario(Long id, String login, String nome) {
        return Jwts.builder()
                .subject(id.toString())
                .claim("login", login)
                .claim("nome", nome)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static String bearer(Long id, String login, String nome) {
        return "Bearer " + tokenParaUsuario(id, login, nome);
    }
}
