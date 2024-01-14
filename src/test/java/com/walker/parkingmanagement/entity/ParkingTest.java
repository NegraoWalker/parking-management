package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.web.dto.ParkingCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parkinglots-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parkinglots-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createCheckin_WithDataValid_ShouldReturnCreatedAndLocation() {
        ParkingCreateDTO createDto = ParkingCreateDTO.builder()
                .licensePlate("WER-1111").brand("FIAT").model("PALIO 1.0")
                .color("AZUL").clientCpf("09191773016")
                .build();

        webTestClient.post().uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("licensePlate").isEqualTo("WER-1111")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO 1.0")
                .jsonPath("color").isEqualTo("AZUL")
                .jsonPath("clientCpf").isEqualTo("09191773016")
                .jsonPath("parkingReceipt").exists()
                .jsonPath("entryDate").exists()
                .jsonPath("vacancyCode").exists();
    }

//    @Test
//    public void criarCheckin_ComRoleCliente_RetornarErrorStatus403() {
//        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
//                .licensePlate("WER-1111").brand("FIAT").model("PALIO 1.0")
//                .color("AZUL").clientCpf("09191773016")
//                .build();
//
//        webTestClient.post().uri("/api/v1/estacionamentos/check-in")
//                .contentType(MediaType.APPLICATION_JSON)
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
//                .bodyValue(createDto)
//                .exchange()
//                .expectStatus().isForbidden()
//                .expectBody()
//                .jsonPath("status").isEqualTo("403")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
//                .jsonPath("method").isEqualTo("POST");
//    }

//    @Test
//    public void criarCheckin_ComDadosInvalidos_RetornarErrorStatus422() {
//        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
//                .licensePlate("").brand("").model("")
//                .color("").clientCpf("")
//                .build();
//
//        webTestClient.post().uri("/api/v1/estacionamentos/check-in")
//                .contentType(MediaType.APPLICATION_JSON)
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
//                .bodyValue(createDto)
//                .exchange()
//                .expectStatus().isEqualTo(422)
//                .expectBody()
//                .jsonPath("status").isEqualTo("422")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
//                .jsonPath("method").isEqualTo("POST");
//    }

//    @Test
//    public void criarCheckin_ComCpfInexistente_RetornarErrorStatus404() {
//        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
//                .licensePlate("WER-1111").brand("FIAT").model("PALIO 1.0")
//                .color("AZUL").clientCpf("33838667000")
//                .build();
//
//        webTestClient.post().uri("/api/v1/estacionamentos/check-in")
//                .contentType(MediaType.APPLICATION_JSON)
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .bodyValue(createDto)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("status").isEqualTo("404")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
//                .jsonPath("method").isEqualTo("POST");
//    }

//    @Sql(scripts = "/sql/estacionamentos/estacionamento-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    @Sql(scripts = "/sql/estacionamentos/estacionamento-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    @Test
//    public void criarCheckin_ComVagasOcupadas_RetornarErrorStatus404() {
//        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
//                .licensePlate("WER-1111").brand("FIAT").model("PALIO 1.0")
//                .color("AZUL").clientCpf("09191773016")
//                .build();
//
//        webTestClient.post().uri("/api/v1/estacionamentos/check-in")
//                .contentType(MediaType.APPLICATION_JSON)
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .bodyValue(createDto)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("status").isEqualTo("404")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
//                .jsonPath("method").isEqualTo("POST");
//    }

//    @Test
//    public void buscarCheckin_ComPerfilAdmin_RetornarDadosStatus200() {
//
//        webTestClient.get()
//                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("licensePlate").isEqualTo("FIT-1020")
//                .jsonPath("brand").isEqualTo("FIAT")
//                .jsonPath("model").isEqualTo("PALIO")
//                .jsonPath("color").isEqualTo("VERDE")
//                .jsonPath("clientCpf").isEqualTo("98401203015")
//                .jsonPath("recibo").isEqualTo("20230313-101300")
//                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
//                .jsonPath("vagaCodigo").isEqualTo("A-01");
//    }

//    @Test
//    public void buscarCheckin_ComPerfilCliente_RetornarDadosStatus200() {
//
//        webTestClient.get()
//                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bob@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("licensePlate").isEqualTo("FIT-1020")
//                .jsonPath("brand").isEqualTo("FIAT")
//                .jsonPath("model").isEqualTo("PALIO")
//                .jsonPath("color").isEqualTo("VERDE")
//                .jsonPath("clientCpf").isEqualTo("98401203015")
//                .jsonPath("recibo").isEqualTo("20230313-101300")
//                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
//                .jsonPath("vagaCodigo").isEqualTo("A-01");
//    }

//    @Test
//    public void buscarCheckin_ComReciboInexistente_RetornarErrorStatus404() {
//
//        webTestClient.get()
//                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-999999")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bob@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("status").isEqualTo("404")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in/20230313-999999")
//                .jsonPath("method").isEqualTo("GET");
//    }

//    @Test
//    public void criarCheckOut_ComReciboExistente_RetornarSucesso() {
//
//        webTestClient.put()
//                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("licensePlate").isEqualTo("FIT-1020")
//                .jsonPath("brand").isEqualTo("FIAT")
//                .jsonPath("model").isEqualTo("PALIO")
//                .jsonPath("color").isEqualTo("VERDE")
//                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
//                .jsonPath("clientCpf").isEqualTo("98401203015")
//                .jsonPath("vagaCodigo").isEqualTo("A-01")
//                .jsonPath("recibo").isEqualTo("20230313-101300")
//                .jsonPath("dataSaida").exists()
//                .jsonPath("valor").exists()
//                .jsonPath("desconto").exists();
//    }

//    @Test
//    public void criarCheckOut_ComReciboInexistente_RetornarErrorStatus404() {
//
//        webTestClient.put()
//                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-000000")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("status").isEqualTo("404")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-000000")
//                .jsonPath("method").isEqualTo("PUT");
//    }

//    @Test
//    public void criarCheckOut_ComRoleCliente_RetornarErrorStatus403() {
//
//        webTestClient.put()
//                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isForbidden()
//                .expectBody()
//                .jsonPath("status").isEqualTo("403")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-101300")
//                .jsonPath("method").isEqualTo("PUT");
//    }

//    @Test
//    public void buscarEstacionamentos_PorclientCpf_RetornarSucesso() {
//
//        PageableDto responseBody = webTestClient.get()
//                .uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=0", "98401203015")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PageableDto.class)
//                .returnResult().getResponseBody();
//
//        org.assertj.colore.api.Assertions.assertThat(responseBody).isNotNull();
//        org.assertj.colore.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
//        org.assertj.colore.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
//        org.assertj.colore.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
//
//        responseBody = webTestClient.get()
//                .uri("/api/v1/estacionamentos/cpf/{cpf}?size=1&page=1", "98401203015")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PageableDto.class)
//                .returnResult().getResponseBody();
//
//        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
//        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
//    }

//    @Test
//    public void buscarEstacionamentos_PorclientCpfComPerfilCliente_RetornarErrorStatus403() {
//
//        webTestClient.get()
//                .uri("/api/v1/estacionamentos/cpf/{cpf}", "98401203015")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isForbidden()
//                .expectBody()
//                .jsonPath("status").isEqualTo("403")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/cpf/98401203015")
//                .jsonPath("method").isEqualTo("GET");
//    }

//    @Test
//    public void buscarEstacionamentos_DoClienteLogado_RetornarSucesso() {
//
//        PageableDto responseBody = webTestClient.get()
//                .uri("/api/v1/estacionamentos?size=1&page=0")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bob@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PageableDto.class)
//                .returnResult().getResponseBody();
//
//        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
//        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
//
//        responseBody = webTestClient.get()
//                .uri("/api/v1/estacionamentos?size=1&page=1")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bob@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(PageableDto.class)
//                .returnResult().getResponseBody();
//
//        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
//        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
//        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
//    }

//    @Test
//    public void buscarEstacionamentos_DoClienteLogadoPerfilAdmin_RetornarErrorStatus403() {
//
//        webTestClient.get()
//                .uri("/api/v1/estacionamentos")
//                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
//                .exchange()
//                .expectStatus().isForbidden()
//                .expectBody()
//                .jsonPath("status").isEqualTo("403")
//                .jsonPath("path").isEqualTo("/api/v1/estacionamentos")
//                .jsonPath("method").isEqualTo("GET");
//    }

}
