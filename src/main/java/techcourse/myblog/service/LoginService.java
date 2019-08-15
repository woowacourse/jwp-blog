package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.support.encryptor.EncryptHelper;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public static final String USER_KEY_IN_SESSION = "user";

    private final UserService userService;
    private final EncryptHelper encryptHelper;

    public LoginService(UserService userService, EncryptHelper encryptHelper) {
        this.userService = userService;
        this.encryptHelper = encryptHelper;
    }

    public void login(HttpSession session, UserLoginRequest userRequest) {
        log.debug("begin");

        User user = userService.findUserByEmail(userRequest.getEmail());
        log.debug("user: {}", user);

        if (user.isWrongPassword(userRequest.getPassword(), encryptHelper)) {
            log.debug("wrong password");

            throw new LoginException("비밀번호 틀림");
        }

        session.setAttribute(USER_KEY_IN_SESSION, user);
    }

    public void logout(HttpSession session) {
        log.debug("begin");

        session.removeAttribute(USER_KEY_IN_SESSION);
    }

    public Optional<User> retrieveLoggedInUser(HttpSession session) {
        if (session == null) {
            return Optional.empty();
        }

        return Optional.ofNullable((User) session.getAttribute(USER_KEY_IN_SESSION));
    }
}
