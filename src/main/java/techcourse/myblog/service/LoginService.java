package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.domain.userinfo.UserEmail;
import techcourse.myblog.domain.userinfo.UserPassword;
import techcourse.myblog.exception.UserMismatchException;
import techcourse.myblog.exception.UserNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private static final String ERROR_USER_NOT_FOUND_MESSAGE = "일치하는 이메일 주소가 없습니다!";
    private static final String ERROR_MISMATCH_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다!";
    private final UserRepository userRepository;

    public void checkLogin(UserEmail email, UserPassword password) {
        if (!existsEmail(email)) {
            log.error("error check login by duplicate email={}", email);
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND_MESSAGE);
        }

        User user = findByEmail(email);
        if (!user.matchPassword(password)) {
            log.error("error check login by unmatch password={}, email={}", password, email);
            throw new UserMismatchException(ERROR_MISMATCH_PASSWORD_MESSAGE);
        }
    }

    private boolean existsEmail(UserEmail email) {
        return findByEmail(email) != null;
    }

    public User findByEmail(String email) {
        return findByEmail(new UserEmail(email));
    }

    public User findByEmail(UserEmail email) {
        return userRepository.findByEmail(email);
    }
}
