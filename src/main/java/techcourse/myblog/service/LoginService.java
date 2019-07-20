package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.repository.UserRepository;

@Service

public class LoginService {
    private static final int NOT_FOUND_RESULT = 0;

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmailAndPassword(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        checkUserByEmail(email);
        checkPassword(email, password);

        return userRepository.findUserByEmailAndPassword(email, password);
    }

    private void checkUserByEmail(String email) {
        if (userRepository.countByEmail(email) == NOT_FOUND_RESULT) {
            throw new FailedLoginException("존재하지 않는 이메일입니다.");
        }
    }

    private void checkPassword(String email, String password) {
        User user = userRepository.findUserByEmailAndPassword(email, password);
        if (user == null) {
            throw new FailedLoginException("비밀번호가 일치하지 않습니다.");
        }
    }
}
