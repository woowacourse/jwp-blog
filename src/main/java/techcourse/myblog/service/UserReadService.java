package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.web.controller.LoginFailedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    public static final String LOGIN_FAIL_MESSAGE = "이메일이나 비밀번호가 올바르지 않습니다";

    private final UserRepository userRepository;

    @Autowired
    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmailAndPassword(UserDto user) {
        return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    public User login(UserDto userDto) {
        return userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .orElseThrow(() -> new LoginFailedException(LOGIN_FAIL_MESSAGE));
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }
}
