package jks.lototronback.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.controller.user.dto.PasswordChange;
import jks.lototronback.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Uue konto registreerimine")
    public void addNewUser(@RequestBody NewUser newUser) {
        userService.addNewUser(newUser);
    }

    @PatchMapping("/user")
    @Operation(summary = "Parooli muutmine")
    public void changePassword(@RequestBody PasswordChange passwordChange) {
        userService.changePassword(passwordChange);
    }

    @DeleteMapping("/removeuser")
    @Operation(summary = "Konto kustutamine ja deaktiveerimine",
            description = "Tabelitest ühtegi kirjet ei eemaldata, 'profile' tabelis muudetakse väärtus D-ks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")})

    public void removeUser(@RequestParam @Parameter Integer userId) {
        userService.removeUser(userId);
    }
}
