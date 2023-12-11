package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.exception.CpfUniqueViolationException;
import com.walker.parkingmanagement.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public Client save(Client client){
        try {
            return clientRepository.save(client);
        }catch (DataIntegrityViolationException exception){
            throw new CpfUniqueViolationException(String.format("CPF '%s' não pode ser cadastrado, já existe no sistema",client.getCpf()));
        }
    }
}
