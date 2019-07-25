package techcourse.myblog.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.presentation.UserRepository;
import techcourse.myblog.service.dto.user.UserResponseDto;
import techcourse.myblog.exception.EmailNotFoundException;
import techcourse.myblog.exception.InvalidPasswordException;

import java.util.Objects;

import static techcourse.myblog.service.user.UserAssembler.convertToDto;

@Service
public class LoginService {
    final private UserRepository userRepository;

    @Autowired
    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto findByEmailAndPassword(final String email, final String password) {
        User retrieveUser = userRepository.findByEmail(Objects.requireNonNull(email))
                .orElseThrow(EmailNotFoundException::new);
        validatePassword(Objects.requireNonNull(password), retrieveUser);
        return convertToDto(retrieveUser);
    }

    private void validatePassword(final String password, final User user) {
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("틀린 비밀번호입니다.");
        }
    }
}
