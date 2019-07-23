package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UserDuplicateException;
import techcourse.myblog.repository.UserRepository;

@Service
public class UserService {
    private static final String ERROR_DUPLICATE_EMAIL_MESSAGE = "이미 가입된 이메일 주소입니다!";
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

    private User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
