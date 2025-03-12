package jks.lototronback.service.profile;

import jks.lototronback.controller.profile.dto.ProfileInfo;
import jks.lototronback.persistence.profile.Profile;
import jks.lototronback.persistence.profile.ProfileMapper;
import jks.lototronback.persistence.profile.ProfileRepository;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;

    @Transactional
    public void updateProfile(Integer userId, ProfileInfo profileInfo) {
        Profile profile = getValidProfile(userId);
        profileMapper.updateProfile(profileInfo, profile);
        profileRepository.save(profile);
    }

    public ProfileInfo getUserProfile(Integer userId) {
        Profile profile = getValidProfile(userId);
        ProfileInfo profileInfo = profileMapper.toProfileInfo(profile);
        return profileInfo;

    }

    private Profile getValidProfile(Integer userId) {
        Profile profile = profileRepository.findProfileBy(userId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));
        return profile;
    }

}
