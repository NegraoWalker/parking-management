package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.ClientVacancy;
import com.walker.parkingmanagement.exception.EntityNotFoundException;
import com.walker.parkingmanagement.repository.ClientVacancyRepository;
import lombok.RequiredArgsConstructor;
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
}
