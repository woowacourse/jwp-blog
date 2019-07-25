package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.LoginDTO;

@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoginUser(LoginDTO loginDTO) {
        User findUser = findByEmail(loginDTO.getEmail());
        if (checkPassword(loginDTO.getPassword(), findUser.getPassword())) {
            return findUser;
        }
        throw new LoginFailException("아이디와 비밀번호가 일치하지 않습니다.");
    }

    private boolean checkPassword(String password, String checkPassword) {
        return password.equals(checkPassword);
    }

    private User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotExistException("해당 아이디를 가진 유저는 존재하지 않습니다.")
                );
    }
}
