package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.InvalidPasswordException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.UserRepository;

import java.util.Objects;

@Service
public class LoginService {
    final private UserRepository userRepository;

    @Autowired
    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findByEmailAndPassword(final String email, final String password) {
        User retrieveUser = userRepository.findByEmail(Objects.requireNonNull(email))
            .orElseThrow(UserNotFoundException::new);
        validatePassword(Objects.requireNonNull(password), retrieveUser);
        return retrieveUser;
    }

    private void validatePassword(final String password, final User user) {
        if (!user.matchPassword(password)) {
            throw new InvalidPasswordException("틀린 비밀번호입니다.");
        }
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
    }
}
