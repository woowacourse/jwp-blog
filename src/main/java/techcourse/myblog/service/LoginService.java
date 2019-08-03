package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.exception.LoginException;
import techcourse.myblog.service.dto.AuthenticationDto;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserRepository;

import java.util.Optional;

@Service
public class LoginService {

    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(AuthenticationDto authenticationDto) {
        User user = userRepository.findByEmail(authenticationDto.getEmail())
                .orElseThrow(LoginException::notFoundEmail);

        return Optional.of(user).filter(u -> u.matchPassword(authenticationDto.getPassword()))
                .orElseThrow(LoginException::notFoundEmail);
    }
}
