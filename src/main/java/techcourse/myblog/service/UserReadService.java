package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.exception.LoginFailedException;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    private final UserRepository userRepository;

    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmailAndPassword(UserDto user) {
        return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(LoginFailedException::new);
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }
}
