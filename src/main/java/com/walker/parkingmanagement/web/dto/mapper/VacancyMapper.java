package com.walker.parkingmanagement.web.dto.mapper;

import com.walker.parkingmanagement.entity.Vacancy;
import com.walker.parkingmanagement.web.dto.VacancyCreateDTO;
import com.walker.parkingmanagement.web.dto.VacancyResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyMapper {
    public static Vacancy toVacancy(VacancyCreateDTO dto){
        return new ModelMapper().map(dto,Vacancy.class);
    }
    public static VacancyResponseDTO toDto(Vacancy vacancy){
        return new ModelMapper().map(vacancy,VacancyResponseDTO.class);
    }
}
