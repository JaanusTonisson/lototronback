package jks.lototronback.controller.user.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedUser {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}

