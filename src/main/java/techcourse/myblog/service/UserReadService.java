package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.AuthenticationFailedException;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.UserRepository;
import techcourse.myblog.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserReadService {
    public static final String NOT_EXIST_USER_MESSAGE = "존재하지 않는 user 입니다";
    public static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";

    private final UserRepository userRepository;

    @Autowired
    public UserReadService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmailAndPassword(UserDto user) {
        return userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return Collections.unmodifiableList(userRepository.findAll());
    }
}
