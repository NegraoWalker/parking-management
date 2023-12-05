package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.jwt.JwtToken;
import com.walker.parkingmanagement.web.dto.LoginUserDTO;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.HttpHeaders;

import java.util.Objects;
import java.util.function.Consumer;

public class JwtAuth {
    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient webTestClient, String username, String password){
        String token = Objects.requireNonNull(webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .bodyValue(new LoginUserDTO(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody()).getToken();
        return httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
