package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.LoginNotMatchedException;

import java.util.Optional;

@Slf4j
@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(UserDto userDto) {
        User user = findByEmail(userDto.getEmail());
        if (user.isNotMatchedPassword(userDto.getPassword())) {
            throw new LoginNotMatchedException();
        }
        return user;
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(LoginNotMatchedException::new);
    }
}
