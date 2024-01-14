package com.walker.parkingmanagement.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingCreateDTO {
    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "A placa do veículo deve seguir o padrão 'XXX-0000'")
    private String licensePlate; //Placa do veiculo
    @NotBlank
    private String brand; //Marca do veiculo
    @NotBlank
    private String model; //Modelo do veiculo
    @NotBlank
    private String color; //Cor do veiculo
    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String clientCpf;
}
