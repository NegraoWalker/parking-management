package com.walker.parkingmanagement.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseUserDTO {
    private Long id;
    private String username;
    private String role;
}
