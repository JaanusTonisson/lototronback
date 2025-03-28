package jks.lototronback.controller.login;

import jks.lototronback.controller.login.dto.LoginResponse;
import jks.lototronback.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public LoginResponse login(@RequestParam String username, @RequestParam String password) {
        return loginService.login(username, password);
    }

}
