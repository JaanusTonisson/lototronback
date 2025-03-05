package jks.lototronback.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @Operation(summary = "Uue konto registreerimine")
 public void addNewUser(@RequestBody @Validated NewUser newUser) {
     userService.addNewUser(newUser);
 }

    @GetMapping("/user")

    public void getValidatedUser(Integer userId) {
        userService.getValidatedUser(userId);
    }
}
