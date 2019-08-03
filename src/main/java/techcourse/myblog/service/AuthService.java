package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotExistUserException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(UserDto userDto) {
        Optional<User> maybeUser = userRepository.findByEmail(userDto.getEmail());
        User user = maybeUser.orElseThrow(() -> new NotExistUserException("해당 이메일로 가입한 유저가 없습니다."));

        if (user.authenticate(userDto.getEmail(), userDto.getPassword())) {
            return user;
        }

        throw new NotMatchAuthenticationException("인증 정보가 일치하지 않습니다.");
    }

    public void logout(HttpSession session) {
        session.removeAttribute("user");
    }
}
