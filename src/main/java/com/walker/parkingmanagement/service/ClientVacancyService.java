package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.ClientVacancy;
import com.walker.parkingmanagement.exception.EntityNotFoundException;
import com.walker.parkingmanagement.repository.ClientVacancyRepository;
import com.walker.parkingmanagement.repository.projection.ClientVacancyProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientVacancyService {
    private final ClientVacancyRepository clientVacancyRepository;

    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy){
        return clientVacancyRepository.save(clientVacancy);
    }

    @Transactional(readOnly = true)
    public ClientVacancy findByParkingReceipt(String parkingReceipt) {
        return clientVacancyRepository.findByParkingReceiptAndDepartureDateIsNull(parkingReceipt)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Recibo '%s' não encontrado no sistema ou check-out já realizado!",parkingReceipt)));
    }

    @Transactional(readOnly = true)
    public long getTotalParkingTimes(String cpf) {
        return clientVacancyRepository.countByClientCpfAndDepartureDateIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClientVacancyProjection> findAllWithClientCpf(String cpf, Pageable pageable) {
        return clientVacancyRepository.findAllByClientCpf(cpf,pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClientVacancyProjection> findAllByUserId(Long id, Pageable pageable) {
        return clientVacancyRepository.findAllByClientUserId(id,pageable);
    }
}
