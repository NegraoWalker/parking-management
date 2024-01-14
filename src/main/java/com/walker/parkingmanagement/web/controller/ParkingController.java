package com.walker.parkingmanagement.web.controller;

import com.walker.parkingmanagement.entity.ClientVacancy;
import com.walker.parkingmanagement.service.ClientVacancyService;
import com.walker.parkingmanagement.service.ParkingService;
import com.walker.parkingmanagement.web.dto.ParkingCreateDTO;
import com.walker.parkingmanagement.web.dto.ParkingResponseDTO;
import com.walker.parkingmanagement.web.dto.mapper.ClientVacancyMapper;
import com.walker.parkingmanagement.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saída de um veículo do estacionamento.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/parking-lots")
public class ParkingController {
    private final ParkingService parkingService;
    private final ClientVacancyService clientVacancyService;



    @Operation(summary = "Operação de check-in", description = "Recurso para dar entrada de um veículo no estacionamento. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso ao recurso criado"),
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Causas possiveis: <br/>" +
                            "- CPF do cliente não cadastrado no sistema; <br/>" +
                            "- Nenhuma vaga livre foi localizada;",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENT",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkin(@RequestBody @Valid ParkingCreateDTO parkingCreateDTO){
        ClientVacancy clientVacancy = ClientVacancyMapper.toClientVacancy(parkingCreateDTO);
        parkingService.checkIn(clientVacancy);
        ParkingResponseDTO parkingResponseDTO = ClientVacancyMapper.toDto(clientVacancy);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{ParkingReceipt}").buildAndExpand(clientVacancy.getParkingReceipt()).toUri();
        return ResponseEntity.created(location).body(parkingResponseDTO);
    }

    @Operation(summary = "Localizar um veículo estacionado", description = "Recurso para retornar um veículo estacionado " +
            "pelo nº do recibo. Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "ParkingReceipt", description = "Número do recibo gerado pelo check-in")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Número do recibo não encontrado.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/check-in/{ParkingReceipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ParkingResponseDTO> getByParkingReceipt(@PathVariable String parkingReceipt){
        ClientVacancy clientVacancy = clientVacancyService.findByParkingReceipt(parkingReceipt);
        ParkingResponseDTO parkingResponseDTO = ClientVacancyMapper.toDto(clientVacancy);
        return ResponseEntity.ok(parkingResponseDTO);
    }

    @Operation(summary = "Operação de check-out", description = "Recurso para dar saída de um veículo do estacionamento. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = { @Parameter(in = PATH, name = "ParkingReceipt", description = "Número do recibo gerado pelo check-in",
                    required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso atualzado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Número do recibo inexistente ou " +
                            "o veículo já passou pelo check-out.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENT",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/check-out/{ParkingReceipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkout(@PathVariable String parkingReceipt) {
        ClientVacancy clientVacancy = parkingService.checkOut(parkingReceipt);
        ParkingResponseDTO dto = ClientVacancyMapper.toDto(clientVacancy);
        return ResponseEntity.ok(dto);
    }
}
