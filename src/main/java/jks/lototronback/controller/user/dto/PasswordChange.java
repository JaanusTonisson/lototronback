package jks.lototronback.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChange {
    private Integer userId;
    private String oldPassword;
    private String newPassword;
}
