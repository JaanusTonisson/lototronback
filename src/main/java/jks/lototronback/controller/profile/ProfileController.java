package jks.lototronback.controller.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jks.lototronback.controller.profile.dto.ProfileInfo;
import jks.lototronback.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    @Operation(summary = "Toob kasutaja profiiliandmed kasutaja ID põhjal" )
    public ProfileInfo getUserProfile(@RequestParam Integer userId) {
        ProfileInfo userProfile = profileService.getUserProfile(userId);
        return userProfile;
    }

    @PutMapping("/profile")
    @Operation(summary = "Kasutaja andmete uuendamine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    public void updateUser(@RequestParam Integer userId, @RequestBody ProfileInfo profileInfo) {
        profileService.updateProfile(userId, profileInfo);
    }
    @DeleteMapping("/profile/image")
    @Operation(summary = "Kustuta profiili pilt")
    public void deleteProfileImage(@RequestParam Integer userId) {
        profileService.deleteProfileImage(userId);
    }

}
