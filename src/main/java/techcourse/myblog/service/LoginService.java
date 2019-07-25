package techcourse.myblog.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.exception.UserNotFoundException;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public static final String USER_ID = "userId";

    private UserRepository userRepository;

    public boolean isLoggedIn(HttpSession session) {
        if (session == null) return false;

        Optional<Long> userId = Optional.ofNullable((Long) session.getAttribute("userId"));

        userId.ifPresent(id -> log.debug("logged in id : {}", id));

        return userId.isPresent();
    }

    public void login(HttpSession session, String email, String password) {
        log.debug("called..!!");

        User user = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));

        log.debug("user: {}", user);

        session.setAttribute(USER_ID, user.getId());
    }

    public void logout(HttpSession session) {
        log.debug("called..!!");

        session.removeAttribute(USER_ID);
    }
}
