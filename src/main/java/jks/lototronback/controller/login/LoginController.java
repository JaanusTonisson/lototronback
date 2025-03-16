package jks.lototronback.controller.login;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.login.dto.LoginResponse;
import jks.lototronback.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    @Operation(summary = "Autentib kasutaja ja tagastab sisselogimise vastuse.")
    public LoginResponse login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // 🔹 Lõpetame vana sessiooni, kui see on olemas
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // 🔹 Loome uue sessiooni
        HttpSession newSession = request.getSession(true);

        // 🔹 Logime kasutaja sisse ja salvestame sessiooni
        LoginResponse loginResponse = loginService.login(username, password);
        newSession.setAttribute("user", username);

        return loginResponse;
    }

    @GetMapping("/check-session")
    @Operation(summary = "Kontrollib, kas kasutaja on sisse logitud.")
    public ResponseEntity<Boolean> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = session != null && session.getAttribute("user") != null;
        return ResponseEntity.ok(isLoggedIn);
    }
}
