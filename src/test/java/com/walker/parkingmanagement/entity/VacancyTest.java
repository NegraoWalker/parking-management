package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.web.dto.VacancyCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vacancies-insert.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vacancies-delete.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VacancyTest {

    @Autowired
    WebTestClient webTestClient;


    @Test
    public void testCreateVacancy_WithValidData_ShouldReturnLocationStatus201() {
        webTestClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-05", "FREE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void testCreateVacancy_WithCodeAlreadyExisting_ShouldReturnErrorMessageWithStatus409() {
        webTestClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-01", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");

    }

    @Test
    public void testCreateVacancy_WithInvalidData_ShouldReturnErrorMessageWithStatus422() {
        webTestClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VacancyCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");

        webTestClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-501", "UNOCCUPIED"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");
    }


    @Test
    public void testFindVacancy_WithCodeExisting_ShouldReturnVacancyWithStatus200() {
        webTestClient
                .get()
                .uri("/api/v1/vacancy/{code}", "A-01")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("FREE");

    }

    @Test
    public void testFindVacancy_WithCodeNonexistent_ShouldReturnErrorMessageWithStatus404() {
        webTestClient
                .get()
                .uri("/api/v1/vacancy/{code}", "A-10")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vacancy/A-10");
    }

    @Test
    public void testFindVacancy_WithUserNotAccessPermission_ShouldReturnErrorMessageWithStatus403() {
        webTestClient
                .get()
                .uri("/api/v1/vacancy/{code}", "A-01")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vacancy/A-01");
    }

    @Test
    public void testCreateVacancy_WithUserNotAccessPermission_ShouldReturnErrorMessageWithStatus403() {
        webTestClient
                .post()
                .uri("/api/v1/vacancy")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .bodyValue(new VacancyCreateDTO("A-05", "OCCUPIED"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vacancy");
    }

}
