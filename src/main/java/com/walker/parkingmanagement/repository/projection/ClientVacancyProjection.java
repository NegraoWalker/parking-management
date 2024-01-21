package com.walker.parkingmanagement.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientVacancyProjection {
    String getLicensePlate(); //Placa do veiculo
    String getBrand(); //Marca do veiculo
    String getModel(); //Modelo do veiculo
    String getColor(); //Cor do veiculo
    String getClientCpf();
    String getReceipt(); //Recibo de pagamento do estacionamento
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getEntryDate(); //Data de entrada do veiculo
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDepartureDate(); //Data de saída do veiculo
    String getVacancyCode(); //Código da vaga do estacionamento
    BigDecimal getValue(); //Valor do estacionamento
    BigDecimal getDiscount(); //Desconto no valor do estacionamento
}
