package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.dto.domain.UserDTO;

@Service
@Transactional
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoginUser(UserDTO userDTO) {
        User user = findByEmail(userDTO.getEmail());

        if (user == null) {
            throw new NotFoundUserException();
        }

        if (!user.matchPassword(userDTO.getPassword())) {
            throw new LoginFailException("아이디와 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    private User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundUserException()
                );
    }
}
