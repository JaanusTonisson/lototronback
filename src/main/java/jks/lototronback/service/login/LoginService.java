package jks.lototronback.service.login;

import jks.lototronback.controller.login.dto.LoginResponse;
import jks.lototronback.infrastructure.Error;
import jks.lototronback.infrastructure.exception.ForbiddenException;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserMapper;
import jks.lototronback.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static jks.lototronback.status.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public LoginResponse login(String username, String password) {
        Optional<User> optionalUser = userRepository.findUserBy(username, password, ACTIVE.getCode());
        User user = optionalUser.orElseThrow(() -> new ForbiddenException(Error.INCORRECT_CREDENTIALS.getMessage(), Error.INCORRECT_CREDENTIALS.getErrorCode()));
        LoginResponse loginResponse = userMapper.toLoginResponse(user);
        return loginResponse;
    }
}
