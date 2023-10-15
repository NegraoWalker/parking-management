package com.walker.parkingmanagement.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUserDTO {
    private String username;
    private String password;
}
