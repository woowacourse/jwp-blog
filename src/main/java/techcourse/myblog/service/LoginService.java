package techcourse.myblog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.UserRepository;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.exception.AuthenticationFailException;

import javax.servlet.http.HttpSession;

@Service
@AllArgsConstructor
public class LoginService {
    public final static String LOGGED_IN_USER_SESSION_KEY = "user";

    private UserRepository userRepository;

    public User authenticate(UserRequestDto userRequestDto) {
        return userRepository.findByEmail(userRequestDto.getEmail())
                .filter(user -> user.isMatch(userRequestDto))
                .orElseThrow(AuthenticationFailException::new);
    }

    public void login(HttpSession session, UserRequestDto userRequestDto) {
        session.setAttribute(LOGGED_IN_USER_SESSION_KEY, authenticate(userRequestDto));
    }

    public void logout(HttpSession session) {
        session.removeAttribute(LOGGED_IN_USER_SESSION_KEY);
    }
}
