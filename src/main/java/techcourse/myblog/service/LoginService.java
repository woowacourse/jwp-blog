package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.AuthenticationDto;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.repository.UserRepository;

@Service
public class LoginService {
    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(AuthenticationDto authenticationDto) {
        User user = userRepository.findByEmail(authenticationDto.getEmail())
                .orElseThrow(() -> new LoginFailException("해당 이메일은 존재하지 않습니다."));
        if (!user.matchPassword(authenticationDto.getPassword())) {
            throw new LoginFailException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }
}
