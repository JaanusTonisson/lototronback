package jks.lototronback.service.user;

import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.persistence.profile.Profile;
import jks.lototronback.persistence.profile.ProfileMapper;
import jks.lototronback.persistence.profile.ProfileRepository;
import jks.lototronback.persistence.role.Role;
import jks.lototronback.persistence.role.RoleRepository;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserMapper;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserService {

    public static final int ROLE_USER = 2;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;



    @Transactional
    public void addNewUser(NewUser newUser) {
        if (userRepository.existsByUsername(newUser.getUserName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Kasutaja on juba olemas");
        }
        User user = createAndSaveUser(newUser);
        createAndSaveProfile(newUser, user);
    }

    public User getValidatedUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ValidationService.throwForeignKeyNotFoundException("userId", userId));
        return user;
    }

    private User createAndSaveUser(NewUser newUser) {
        User user = createUser(newUser);
        userRepository.save(user);
        return user;
    }

    private User createUser(NewUser newUser) {
        Role role = roleRepository.getReferenceById(ROLE_USER);
        User user = userMapper.toUser(newUser);
        user.setRole(role);
        return user;
    }


    private void createAndSaveProfile(NewUser newUser, User user) {
        Profile profile = createProfile(newUser, user);
        profileRepository.save(profile);
    }

    private Profile createProfile(NewUser newUser, User user) {
        Profile profile = profileMapper.toProfile(newUser);
        profile.setUser(user);
        return profile;
    }
}

