package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.exception.LoginFailedException;
import techcourse.myblog.service.exception.NotFoundUserException;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(UserDto userDto) {
        return userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .orElseThrow(LoginFailedException::new);
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundUserException::new);
    }

    boolean isExist(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
}
