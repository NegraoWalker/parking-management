package com.walker.parkingmanagement.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyResponseDTO {
    private Long id;
    private String code;
    private String status;
}
