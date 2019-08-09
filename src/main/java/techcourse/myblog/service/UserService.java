package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.exception.ValidUserException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    public static final String EMAIL_DUPLICATE_MESSAGE = "이미 사용중인 이메일입니다.";
    public static final String PASSWORD_INVALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    public static final String EMAIL_OR_PASSWORD_NOT_MATCH = "존재하지 않는 이메일 또는 비밀번호가 틀립니다.";

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long save(UserDto.Register userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        verifyPassword(userDto);

        final User user = userDto.toUser();

        return userRepository.save(user).getId();
    }

    private void verifyPassword(UserDto.Register userDto) {
        if (!userDto.isValidPassword()) {
            throw new ValidUserException(PASSWORD_INVALID_MESSAGE, "password");
        }
    }

    private void verifyDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidUserException(EMAIL_DUPLICATE_MESSAGE, "email");
        }
    }

    @Transactional(readOnly = true)
    public User login(UserDto.Register userDto) {
        final Optional<User> user = userRepository.findByEmail(userDto.getEmail());

        if (!user.isPresent() || !user.get().authenticate(userDto.getPassword())) {
            throw new ValidUserException(EMAIL_OR_PASSWORD_NOT_MATCH, "password");
        }

        return user.get();
    }

    public List<UserDto.Response> findAllExceptPassword() {
        final List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserDto.Response::createByUser)
                .collect(Collectors.toList());
    }

    public UserDto.Response getUserById(Long id) {
        final User user = findById(id);

        return UserDto.Response.createByUser(user);
    }

    public UserDto.Response update(UserDto.Update userDto) {
        final User user = findById(userDto.getId());

        user.update(userDto.toUser());

        return UserDto.Response.createByUser(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. : " + id));
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email 입니다."));
    }
}


