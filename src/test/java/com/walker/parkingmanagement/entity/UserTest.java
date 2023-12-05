package com.walker.parkingmanagement.entity;

import com.walker.parkingmanagement.web.dto.CreateUserDTO;
import com.walker.parkingmanagement.web.dto.ResponseUserDTO;
import com.walker.parkingmanagement.web.dto.UpdatePasswordDTO;
import com.walker.parkingmanagement.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

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

    @Test
    public void testCreateUser_WithUsernameRepeated_ShouldReturnErrorMessageWithStatus409(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .post()
                .uri("/api/v1/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDTO("ana@email.com","123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(409);
    }

    @Test
    public void testFindUser_WithIdExisting_ShouldReturnUserWithStatus200(){
        ResponseUserDTO responseUserBodyDTO = webTestClient
                .get()
                .uri("/api/v1/users/find-user/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseUserDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getUsername()).isEqualTo("ana@email.com");
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getRole()).isEqualTo("ADMIN");

         responseUserBodyDTO = webTestClient
                .get()
                .uri("/api/v1/users/find-user/101")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseUserDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getUsername()).isEqualTo("bia@email.com");
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getRole()).isEqualTo("CLIENT");

        responseUserBodyDTO = webTestClient
                .get()
                .uri("/api/v1/users/find-user/101")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"bia@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseUserDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getUsername()).isEqualTo("bia@email.com");
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void testFindUser_WithIdNonexistent_ShouldReturnErrorMessageWithStatus404(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .get()
                .uri("/api/v1/users/find-user/0")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(404);
    }

    @Test
    public void testFindUser_WithUserClientFindOtherClient_ShouldReturnErrorMessageWithStatus403(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .get()
                .uri("/api/v1/users/find-user/102")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(403);
    }

    @Test
    public void testUpdatePasswordUser_WithDataValid_ShouldReturnStatus204(){
        webTestClient
                .put()
                .uri("/api/v1/users/update-password/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("123456","245678","245678"))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .put()
                .uri("/api/v1/users/update-password/101")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"bia@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("123456","245678","245678"))
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    public void testUpdatePasswordUser_WithUsersDifferent_ShouldReturnErrorMessageWithStatus403(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .put()
                .uri("/api/v1/users/update-password/0")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("123456","245678","245678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(403);

        webTestClient
                .put()
                .uri("/api/v1/users/update-password/0")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"bia@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("123456","245678","245678"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(403);
    }

    @Test
    public void testUpdatePasswordUser_WithDataInvalid_ShouldReturnErrorMessageWithStatus422(){
        ErrorMessage responseUserBodyDTO = webTestClient
                .put()
                .uri("/api/v1/users/update-password/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("","",""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);

        webTestClient
                .put()
                .uri("/api/v1/users/update-password/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("12345","24567","24567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);

        webTestClient
                .put()
                .uri("/api/v1/users/update-password/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("1234567","2456789","2456789"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(422);
    }

    @Test
    public void testUpdatePasswordUser_WithPasswordInvalid_ShouldReturnErrorMessageWithStatus400() {
        ErrorMessage responseUserBodyDTO = webTestClient
                .put()
                .uri("/api/v1/users/update-password/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("123456", "234567", "000000"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(400);

        webTestClient
                .put()
                .uri("/api/v1/users/update-password/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdatePasswordDTO("000000", "234567", "234567"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(400);
    }

    @Test
    public void testFindAllUsers_WithIdExisting_ShouldReturnUserWithStatus200(){
        List<ResponseUserDTO> responseUserBodyDTO =  webTestClient
                .get()
                .uri("/api/v1/users/find-user-all")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ResponseUserDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.size()).isEqualTo(3);

    }
    @Test
    public void testFindAllUsers_WithUserWithoutPermission_ShouldReturnErrorMessageWithStatus403() {
        ErrorMessage responseUserBodyDTO = webTestClient
                .get()
                .uri("/api/v1/users/find-user-all")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseUserBodyDTO.getStatus()).isEqualTo(403);
    }

    @Test
    public void testDeleteUser_ShouldReturnStatus204(){
        webTestClient
                .delete()
                .uri("/api/v1/users/delete-user/100")
                .headers(JwtAuth.getHeaderAuthorization(webTestClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

}