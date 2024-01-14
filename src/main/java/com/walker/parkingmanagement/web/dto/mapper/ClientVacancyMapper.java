package com.walker.parkingmanagement.web.dto.mapper;

import com.walker.parkingmanagement.entity.ClientVacancy;
import com.walker.parkingmanagement.web.dto.ParkingCreateDTO;
import com.walker.parkingmanagement.web.dto.ParkingResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {
    public static ClientVacancy toClientVacancy(ParkingCreateDTO parkingCreateDTO){
        return new ModelMapper().map(parkingCreateDTO,ClientVacancy.class);
    }

    public static ParkingResponseDTO toDto(ClientVacancy clientVacancy){
        return new ModelMapper().map(clientVacancy,ParkingResponseDTO.class);
    }
}
