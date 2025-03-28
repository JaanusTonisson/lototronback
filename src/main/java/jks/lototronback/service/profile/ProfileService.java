package jks.lototronback.service.profile;

import jks.lototronback.controller.profile.dto.ProfileInfo;
import jks.lototronback.persistence.profile.Profile;
import jks.lototronback.persistence.profile.ProfileMapper;
import jks.lototronback.persistence.profile.ProfileRepository;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.util.BytesConverter;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jks.lototronback.persistence.userimage.UserImage;
import jks.lototronback.persistence.userimage.UserImageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final UserImageRepository userImageRepository;

    @Transactional
    public void updateProfile(Integer userId, ProfileInfo profileInfo) {
        Profile profile = getValidProfile(userId);
        profileMapper.updateProfile(profileInfo, profile);
        profileRepository.save(profile);
        if (profileInfo.getImageData() != null && !profileInfo.getImageData().isEmpty()) {
            handleProfileImageUpdate(userId, profileInfo.getImageData());
        }

    }

    public ProfileInfo getUserProfile(Integer userId) {
        Profile profile = getValidProfile(userId);
        ProfileInfo profileInfo = profileMapper.toProfileInfo(profile);
        Optional<UserImage> userImageOptional = userImageRepository.findUserImageByUserId(userId);
        if (userImageOptional.isPresent()) {
            String imageData = BytesConverter.bytesArrayToString(userImageOptional.get().getData());
            profileInfo.setImageData(imageData);
        }
        return profileInfo;

    }

    @Transactional
    public void handleProfileImageUpdate(Integer userId, String imageData) {
        Optional<UserImage> userImageOptional = userImageRepository.findUserImageByUserId(userId);

        if (imageData == null || imageData.isEmpty()) {
            userImageOptional.ifPresent(userImageRepository::delete);
            return;
        }

        if (userImageOptional.isEmpty()) {
            UserImage userImage = new UserImage();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));
            userImage.setUser(user);
            userImage.setData(BytesConverter.stringToBytesArray(imageData));
            userImageRepository.save(userImage);
        } else {
            UserImage userImage = userImageOptional.get();
            userImage.setData(BytesConverter.stringToBytesArray(imageData));
            userImageRepository.save(userImage);
        }
    }
    private Profile getValidProfile(Integer userId) {
        return profileRepository.findProfileBy(userId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));
    }

    @Transactional
    public void deleteProfileImage(Integer userId) {
        Optional<UserImage> userImageOptional = userImageRepository.findUserImageByUserId(userId);

        userImageOptional.ifPresent(userImageRepository::delete);
    }
}
