package jks.lototronback.controller.login;

import jks.lototronback.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    public void login(@RequestParam String username, @RequestParam String password) {
        loginService.login(username, password);
    }

}
