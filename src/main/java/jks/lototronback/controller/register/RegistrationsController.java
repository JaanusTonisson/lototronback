package jks.lototronback.controller.register;

import jks.lototronback.service.register.RegistrationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegistrationsController {
    private final RegistrationsService registrationsService;

    @GetMapping("/user-registrations")
    public List<RegisterInfo> findUserRegistrations(@RequestParam int userId) {
        List<RegisterInfo> userRegistrations = registrationsService.findUserRegistrations(userId);
        return userRegistrations;


    }
}
