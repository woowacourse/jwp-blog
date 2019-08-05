package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.MisMatchPasswordException;
import techcourse.myblog.domain.exception.UserNotFoundException;
import techcourse.myblog.domain.repository.UserRepository;

@Service
public class LoginService {
    private final static String USER_ERROR = "해당 Email의 사용자를 찾을 수 없습니다.";
    private final static String PASSWORD_ERROR = "비밀번호가 일치하지 않습니다.";

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User login(String email, String password) {
        if (notExistUserEmail(email)) {
            throw new UserNotFoundException(USER_ERROR);
        }
        if (!matchEmailAndPassword(email, password)) {
            throw new MisMatchPasswordException(PASSWORD_ERROR);
        }
        return userRepository.findUserByEmail(email);
    }

    private boolean notExistUserEmail(String email) {
        return userRepository.findUserByEmail(email) == null;
    }

    private boolean matchEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmail(email).matchPassword(password);
    }
}
