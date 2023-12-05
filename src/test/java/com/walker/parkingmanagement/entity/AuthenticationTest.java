package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.jwt.JwtToken;
import com.walker.parkingmanagement.web.dto.LoginUserDTO;
import com.walker.parkingmanagement.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testAuthentication_WithCredentialsValid_ShouldReturnTokenWithStatus200() {
        JwtToken responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void testAuthentication_WithCredentialsInvalid_ShouldReturnErrorMessageStatus400() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("invalido@email.com", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("ana@email.com", "000000"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void testAuthentication_WithUsernameInvalid_ShouldReturnErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void testAuthentication_WithPasswordInvalid_ShouldReturnErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("ana@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("ana@email.com", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/users/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginUserDTO("ana@email.com", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }
}
