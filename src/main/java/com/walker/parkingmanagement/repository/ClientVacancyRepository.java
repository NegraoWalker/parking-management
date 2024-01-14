package com.walker.parkingmanagement.repository;

import com.walker.parkingmanagement.entity.ClientVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy,Long> {
    Optional<ClientVacancy> findByParkingReceiptAndDepartureDateIsNull(String parkingReceipt);

    long countByClientCpfAndDepartureDateIsNotNull(String cpf);
}
