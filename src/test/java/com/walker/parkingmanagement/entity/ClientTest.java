package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.web.dto.*;
import com.walker.parkingmanagement.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientTest {

    @Autowired
    WebTestClient webTestClient;


    @Test
    public void testCreateClient_WithValidData_ShouldReturnClientCreatedWithStatus201(){
        ClientResponseDto responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClientCreateDto("Tobias Ferreira", "91191064085"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Tobias Ferreira");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("91191064085");
    }

    @Test
    public void testCreateClient_WithCpfAlreadyRegistered_ShouldReturnErrorMessageStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClientCreateDto("Tobias Ferreira", "55352517047"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void testCreateClient_WithDataInvalid_ShouldReturnErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClientCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClientCreateDto("Bobb", "00000000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "toby@email.com", "123456"))
                .bodyValue(new ClientCreateDto("Bobb", "911.910.640-85"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void testCreateClient_WithUserNotAllowed_ShouldReturnErrorMessageStatus403() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new ClientCreateDto("Tobias Ferreira", "91191064085"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void testSearchClient_WithIdExistingForAdmin_ShouldReturnClientWithStatus200() {
        ClientResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/10")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
    }

    @Test
    public void testSearchClient_WithIdNonexistentForAdmin_ShouldReturnErrorMessageWithStatus404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/0")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void testSearchClient_WithIdExistingForClient_ShouldReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/0")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void testSearchClient_WithPaginationForAdmin_ShouldReturnClienteWithStatus200() {
        PageableDTO responseBody = webTestClient
                .get()
                .uri("/api/v1/clients")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);

        responseBody = webTestClient
                .get()
                .uri("/api/v1/clients?size=1&page=1")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void testSearchClient_WithPaginationForClient_ShouldReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clients")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void testSearchClient_WithDataTokenClient_ShouldReturnClientWithStatus200() {
        ClientResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/details")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("79074426050");
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Bianca Silva");
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);

    }

    @Test
    public void testSearchClient_WithDataTokenAdmin_ShouldReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/details")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }
}
