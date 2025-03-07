package jks.lototronback.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user")
    public void getValidatedUser(@RequestParam Integer userId) {
        userService.getValidatedUser(userId);
    }
}
