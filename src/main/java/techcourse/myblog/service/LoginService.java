package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.exception.NotFindUserEmailException;
import techcourse.myblog.exception.UnequalPasswordException;
import techcourse.myblog.repository.UserRepository;

import java.util.Optional;

@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(LoginDto loginDto) {
        Optional<User> findUser = userRepository.findByEmail(loginDto.getEmail());

        checkExistEmail(findUser);
        checkEqualPassword(findUser, loginDto);

        return findUser.get();
    }

    private void checkExistEmail(Optional<User> findUser) {
        if (!findUser.isPresent()) {
            throw new NotFindUserEmailException();
        }
    }

    private void checkEqualPassword(Optional<User> findUser, LoginDto loginDto) {
        String userPassword = findUser.get().getPassword();

        if (!userPassword.equals(loginDto.getPassword())) {
            throw new UnequalPasswordException();
        }
    }
}
