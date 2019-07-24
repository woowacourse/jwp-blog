package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean matchEmailAndPassword(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            return userRepository.findUserByEmail(email).matchPassword(password);
        }
        return false;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
