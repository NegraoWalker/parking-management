package com.walker.parkingmanagement.web.dto.mapper;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.web.dto.ClientCreateDTO;
import com.walker.parkingmanagement.web.dto.ClientResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
    public static Client toClient(ClientCreateDTO clientCreateDto){
        return new ModelMapper().map(clientCreateDto,Client.class);
    }
    public static ClientResponseDTO toDto(Client client){
        return new ModelMapper().map(client, ClientResponseDTO.class);
    }
}
