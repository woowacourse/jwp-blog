package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.UserRepository;

@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoginUser(LoginDto loginDto) {
        User user = findUser(loginDto.getEmail());
        if (!user.checkPassword(loginDto.getPassword())) {
            throw new LoginFailException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("아이디가 없습니다."));
    }
}
