package com.walker.parkingmanagement.repository;

import com.walker.parkingmanagement.entity.ClientVacancy;
import com.walker.parkingmanagement.repository.projection.ClientVacancyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy,Long> {
    Optional<ClientVacancy> findByParkingReceiptAndDepartureDateIsNull(String parkingReceipt);

    long countByClientCpfAndDepartureDateIsNotNull(String cpf);

    Page<ClientVacancyProjection> findAllByClientCpf(String cpf, Pageable pageable);

    Page<ClientVacancyProjection> findAllByClientUserId(Long id, Pageable pageable);
}
