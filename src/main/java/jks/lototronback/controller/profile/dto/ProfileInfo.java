package jks.lototronback.controller.profile.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfo {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String imageData;
}

