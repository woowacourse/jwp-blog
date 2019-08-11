package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.exception.LoginFailedException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmailAndPassword(UserDto userDto) {
        return userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .orElseThrow(LoginFailedException::new);
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }
}
