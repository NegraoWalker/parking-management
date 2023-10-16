package com.walker.parkingmanagement.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;


}
