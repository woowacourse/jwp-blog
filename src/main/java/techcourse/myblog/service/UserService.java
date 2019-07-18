package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
