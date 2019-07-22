package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.exception.NotFindUserEmailException;
import techcourse.myblog.exception.UnequalPasswordException;
import techcourse.myblog.repository.UserRepository;

@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(LoginDto loginDto) {
        User findUser = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(NotFindUserEmailException::new);

        checkEqualPassword(findUser, loginDto.getPassword());
        return findUser;
    }

    private void checkEqualPassword(User findUser, String inputPassword) {
        if (!findUser.isEqualPassword(inputPassword)) {
            throw new UnequalPasswordException();
        }
    }
}
