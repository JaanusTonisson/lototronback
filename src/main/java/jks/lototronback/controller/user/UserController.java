package jks.lototronback.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Uue konto registreerimine")
 public void addNewUser(@RequestBody NewUser newUser) {
     userService.addNewUser(newUser);
 }
}
