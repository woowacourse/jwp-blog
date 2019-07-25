package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserEditParams;
import techcourse.myblog.exception.UserDuplicateException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ERROR_DUPLICATE_EMAIL_MESSAGE = "이미 가입된 이메일 주소입니다!";
    private final UserRepository userRepository;

    public User save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserDuplicateException(ERROR_DUPLICATE_EMAIL_MESSAGE);
        }

        log.debug("save user={}", user);
        return userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User update(Long id, UserEditParams userEditParams) {
        log.debug("update user params={} by id={}", userEditParams, id);

        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.update(userEditParams);
        return user;
    }

    public void deleteUser(Long id) {
        log.debug("delete user id={}", id);
        userRepository.deleteById(id);
    }
}
