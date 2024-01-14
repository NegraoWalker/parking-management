package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.exception.CpfUniqueViolationException;
import com.walker.parkingmanagement.exception.EntityNotFoundException;
import com.walker.parkingmanagement.repository.ClientRepository;
import com.walker.parkingmanagement.repository.projection.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Cliente id=%s não encontrado no sistema",id)));
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> getByAll(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Client searchByUserId(Long id) {
        return clientRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Client findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(()-> new EntityNotFoundException(String.format("Cliente com CPF '%s' não encontrado!",cpf)));
    }
}
