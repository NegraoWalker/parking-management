package com.walker.parkingmanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingResponseDTO {
    private String licensePlate; //Placa do veiculo
    private String brand; //Marca do veiculo
    private String model; //Modelo do veiculo
    private String color; //Cor do veiculo
    private String clientCpf;
    private String receipt; //Recibo de pagamento do estacionamento
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime entryDate; //Data de entrada do veiculo
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime departureDate; //Data de saída do veiculo
    private String vacancyCode; //Código da vaga do estacionamento
    private BigDecimal value; //Valor do estacionamento
    private BigDecimal discount; //Desconto no valor do estacionamento
}
