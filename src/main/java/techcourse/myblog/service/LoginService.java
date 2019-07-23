package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRepository;
import techcourse.myblog.web.UserRequestDto;

import java.util.NoSuchElementException;

@Service
public class LoginService {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(final UserRequestDto.LoginRequestDto loginRequestDto) {
        return findByLoginRequestDto(loginRequestDto);
    }

    private User findByLoginRequestDto(final UserRequestDto.LoginRequestDto loginRequestDto) {
        return userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(NoSuchElementException::new);
    }
}
