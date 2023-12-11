package com.walker.parkingmanagement.web.dto.mapper;

import com.walker.parkingmanagement.entity.Client;
import com.walker.parkingmanagement.web.dto.ClientCreateDto;
import com.walker.parkingmanagement.web.dto.ClientResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
    public static Client toClient(ClientCreateDto clientCreateDto){
        return new ModelMapper().map(clientCreateDto,Client.class);
    }
    public static ClientResponseDto toDto(Client client){
        return new ModelMapper().map(client,ClientResponseDto.class);
    }
}
