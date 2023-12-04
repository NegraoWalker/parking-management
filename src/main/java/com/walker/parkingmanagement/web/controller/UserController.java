package com.walker.parkingmanagement.web.controller;

import com.walker.parkingmanagement.entity.User;
import com.walker.parkingmanagement.service.UserService;
import com.walker.parkingmanagement.web.dto.CreateUserDTO;
import com.walker.parkingmanagement.web.dto.ResponseUserDTO;
import com.walker.parkingmanagement.web.dto.UpdatePasswordDTO;
import com.walker.parkingmanagement.web.dto.mapper.UserMapper;
import com.walker.parkingmanagement.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários",description = "Contém todas as operações relativas aos recursos de cadastro, edição, leitura e exclusão de usuário")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Cria um novo usuário", description = "Recurso para criar um novo usuário",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Usuário [e-mail] já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/create-user")
    public ResponseEntity<ResponseUserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        User userCreated = userService.save(UserMapper.toUser(createUserDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(userCreated));
    }

    @Operation(summary = "Recupera um usuário pelo id", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN|CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/find-user/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENT') AND #id == authentication.principal.id)")
    public ResponseEntity<ResponseUserDTO> findUserById(@PathVariable Long id){
        User userFindById = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDTO(userFindById));
    }

    @Operation(summary = "Atualiza senha", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN|CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/update-password/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT') AND (#id == authentication.principal.id)")
    public ResponseEntity<ResponseUserDTO> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User userUpdatePassword = userService.update(id,updatePasswordDTO.getCurrentPassword(),updatePasswordDTO.getNewPassword(),updatePasswordDTO.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(UserMapper.toDTO(userUpdatePassword));
    }

    @Operation(summary = "Lista todos os usuários", description = "Requisição exige um Bearer Token. Acesso restrito a ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista com todos os usuários cadastrados",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseUserDTO.class)))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/find-user-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseUserDTO>> findUserAll(){
        List<User> usersFindAll = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toListDTO(usersFindAll));
    }

    @Operation(summary = "Deleta usuário", description = "Recurso para deletar usuário",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @DeleteMapping("/delete-user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
