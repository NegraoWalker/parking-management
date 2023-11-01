package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.web.dto.CreateUserDTO;
import com.walker.parkingmanagement.web.dto.ResponseUserDTO;
import com.walker.parkingmanagement.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testCreateUser_WithUsernameAndPasswordValid_ShouldReturnUserCreatedWithStatus201(){
        ResponseUserDTO responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("tody@email.com","123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseUserDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getUsername()).isEqualTo("tody@email.com");
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void testCreateUser_WithUsernameInvalid_ShouldReturnErrorMessageWithStatus422(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);

        responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("tody@","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);

        responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("tody@email.","123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);
    }

    @Test
    public void testCreateUser_WithPasswordInvalid_ShouldReturnErrorMessageWithStatus422(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("tody@email.com",""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);

        responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("tody@email.com","12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);

        responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("tody@email.com","1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);
    }
}