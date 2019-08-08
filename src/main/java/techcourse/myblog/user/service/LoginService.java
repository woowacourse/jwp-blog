package techcourse.myblog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.user.User;
import techcourse.myblog.user.dto.UserResponse;
import techcourse.myblog.user.exception.InvalidPasswordException;
import techcourse.myblog.user.exception.UserNotFoundException;
import techcourse.myblog.user.persistence.UserRepository;

import java.util.Objects;

import static techcourse.myblog.user.service.UserAssembler.convertToDto;

@Service
public class LoginService {
    final private UserRepository userRepository;

    @Autowired
    public LoginService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse findByEmailAndPassword(final String email, final String password) {
        User retrieveUser = userRepository.findByEmail(Objects.requireNonNull(email))
                .orElseThrow(UserNotFoundException::new);
        validatePassword(Objects.requireNonNull(password), retrieveUser);
        return convertToDto(retrieveUser);
    }

    private void validatePassword(final String password, final User user) {
        if (!user.match(password)) {
            throw new InvalidPasswordException();
        }
    }

    public UserResponse findByEmail(final String email) {
        return convertToDto(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
    }
}
