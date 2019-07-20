package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.exception.LoginException;
import techcourse.myblog.repo.UserRepository;
import techcourse.myblog.user.AuthenticationDto;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpSession;

@Service
public class LoginService {

    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(AuthenticationDto authenticationDto, HttpSession httpSession) {
        User user = userRepository.findByEmail(authenticationDto.getEmail())
                .orElseThrow(LoginException::notFoundEmail);
        checkValidLogin(authenticationDto, user);
        return user;
    }

    private void checkValidLogin(AuthenticationDto authenticationDto, User user) {
        if (!user.matchPassword(authenticationDto.getPassword())) {
            throw LoginException.notMatchPassword();
        }
    }

}
