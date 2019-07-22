package techcourse.myblog.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    static final String EMAIL_DUPLICATE_MESSAGE = "이미 사용중인 이메일입니다.";
    static final String PASSWORD_INVALID_MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";

    private final UserRepository userRepository;

    public Long save(UserDto.Register userDto) {
        verifyDuplicateEmail(userDto.getEmail());
        verifyPassword(userDto);

        User user = userDto.toUser();

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

    public UserSession login(UserDto.Register userDto) {
        String decodedPassword = User.authenticate(userDto.getPassword());

        User user = userRepository.findByEmailAndPassword(userDto.getEmail(), decodedPassword)
                .orElseThrow(() -> new ValidUserException("존재하지 않는 이메일 또는 비밀번호가 틀립니다.", "password"));

        return UserSession.createByUser(user);
    }

    public List<UserDto.Response> findAllExceptPassword() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto.Response::createByUser)
                .collect(Collectors.toList());
    }

    public UserDto.Response findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find User : " + id));

        return UserDto.Response.createByUser(user);
    }

    public UserDto.Response update(Long id, UserDto.Update userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find User : " + id));

        user.update(userDto.toUser(user));

        return UserDto.Response.createByUser(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}


