package jks.lototronback.service.user;

import jks.lototronback.controller.user.dto.NewUser;
import jks.lototronback.persistence.role.Role;
import jks.lototronback.persistence.role.RoleRepository;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserMapper;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    public static final int ROLE_USER = 2;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User addNewUser(NewUser newUser) {
        return createAndSaveUser(newUser);
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



}

