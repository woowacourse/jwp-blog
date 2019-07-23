package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRepository;
import techcourse.myblog.exception.NoSuchUserException;
import techcourse.myblog.exception.PasswordMismatchException;
import techcourse.myblog.web.UserRequestDto;

@Service
public class LoginService {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(final UserRequestDto.LoginRequestDto loginRequestDto) {
        User user = findByLoginRequestDto(loginRequestDto);

        if (user.isSamePassword(loginRequestDto.getPassword())) {
            return user;
        }
        throw new PasswordMismatchException("비밀번호를 제대로 입력하세요");
    }

    private User findByLoginRequestDto(final UserRequestDto.LoginRequestDto loginRequestDto) {
        return userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(NoSuchUserException::new);
    }
}
