package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UserNotFoundException;
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
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null) {
            throw new UserNotFoundException(ERROR_DUPLICATE_EMAIL_MESSAGE);
        }
        return userRepository.save(user);
    }
}
