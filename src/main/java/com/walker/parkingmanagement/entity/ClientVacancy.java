package com.walker.parkingmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "clients_have_vacancies")
@EntityListeners(AuditingEntityListener.class)
public class ClientVacancy { //Classe de relacionamento entre clientes e vagas
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number_parking_receipt", nullable = false, unique = true, length = 15)
    private String parkingReceipt; //Recibo do estacionamento
    @Column(name = "licence_plate", nullable = false, unique = false, length = 8)
    private String licensePlate; //Placa do veiculo
    @Column(name = "brand", nullable = false, unique = false, length = 45)
    private String brand; //Marca do veiculo
    @Column(name = "model", nullable = false, unique = false, length = 45)
    private String model; //Modelo do veiculo
    @Column(name = "color", nullable = false, unique = false, length = 45)
    private String color; //Cor do veiculo
    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate; //Data de entrada do veiculo
    @Column(name = "departure_date")
    private LocalDateTime departureDate; //Data de saída do veiculo
    @Column(name = "value", columnDefinition = "decimal(7,2)")
    private BigDecimal value; //Valor do estacionamento
    @Column(name = "discount", columnDefinition = "decimal(7,2)")
    private BigDecimal discount; //Desconto no valor do estacionamento

    @ManyToOne
    @JoinColumn(name = "id_client",nullable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "id_vacancy",nullable = false)
    private Vacancy vacancy;

    //Audit fields:
    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientVacancy that = (ClientVacancy) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
