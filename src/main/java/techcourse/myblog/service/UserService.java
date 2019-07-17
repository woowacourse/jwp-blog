package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.exception.DuplicatedUserException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicatedUserException();
        }
        userRepository.save(user);
    }
}
