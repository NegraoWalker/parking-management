package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.entity.ClientVacancy;
import com.walker.parkingmanagement.entity.Vacancy;
import com.walker.parkingmanagement.utilities.ParkingUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingService {
    private final ClientVacancyService clientVacancyService;
    private final ClientService clientService;
    private final VacancyService vacancyService;

    @Transactional
    public ClientVacancy checkIn(ClientVacancy clientVacancy){
        Client client = clientService.findByCpf(clientVacancy.getClient().getCpf());
        clientVacancy.setClient(client);

        Vacancy vacancy = vacancyService.findByVacancyFree();
        vacancy.setStatus(Vacancy.StatusVacancy.OCCUPIED);
        clientVacancy.setVacancy(vacancy);

        clientVacancy.setEntryDate(LocalDateTime.now());
        clientVacancy.setParkingReceipt(ParkingUtilities.generateReceipt());

        return clientVacancyService.save(clientVacancy);
    }

    @Transactional
    public ClientVacancy checkOut(String parkingReceipt) {
        ClientVacancy clientVacancy = clientVacancyService.findByParkingReceipt(parkingReceipt);

        LocalDateTime departureDate = LocalDateTime.now();

        BigDecimal value = ParkingUtilities.calculateCost(clientVacancy.getEntryDate(), departureDate);
        clientVacancy.setValue(value);

        long totalOfTimes = clientVacancyService.getTotalParkingTimes(clientVacancy.getClient().getCpf());

        BigDecimal discount = ParkingUtilities.calculateDiscount(value, totalOfTimes);
        clientVacancy.setDiscount(discount);

        clientVacancy.setDepartureDate(departureDate);
        clientVacancy.getVacancy().setStatus(Vacancy.StatusVacancy.FREE);

        return clientVacancyService.save(clientVacancy);
    }
}
