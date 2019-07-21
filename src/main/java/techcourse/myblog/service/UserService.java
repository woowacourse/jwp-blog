package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UserDuplicateException;
import techcourse.myblog.exception.UserMismatchException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.UserRepository;

@Service
public class UserService {
    private static final String ERROR_DUPLICATE_EMAIL_MESSAGE = "이미 가입된 이메일 주소입니다!";
    private static final String ERROR_USER_NOT_FOUND_MESSAGE = "일치하는 이메일 주소가 없습니다!";
    private static final String ERROR_MISMATCH_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다!";
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        if (existsEmail(user.getEmail())) {
            throw new UserDuplicateException(ERROR_DUPLICATE_EMAIL_MESSAGE);
        }
        return userRepository.save(user);
    }

    private boolean existsEmail(String email) {
        return findByEmail(email) != null;
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void checkLogin(String email, String password) {
        if (!existsEmail(email)) {
            throw new UserNotFoundException(ERROR_USER_NOT_FOUND_MESSAGE);
        }

        User user = findByEmail(email);
        if (!user.matchPassword(password)) {
            throw new UserMismatchException(ERROR_MISMATCH_PASSWORD_MESSAGE);
        }
    }
}
