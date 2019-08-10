package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.exception.LogInException;
import techcourse.myblog.service.dto.LoginUserDto;

import static techcourse.myblog.service.exception.LogInException.NOT_FOUND_USER_MESSAGE;
import static techcourse.myblog.service.exception.LogInException.PASSWORD_FAIL_MESSAGE;

@Service
public class LogInService {
    private UserRepository userRepository;

    public LogInService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginUserDto logIn(LogInInfoDto logInInfoDto) {
        User logInUser = userRepository.findByEmail(logInInfoDto.getEmail())
                .orElseThrow(() -> new LogInException(NOT_FOUND_USER_MESSAGE));

        if (logInUser.matchPassword(logInInfoDto.getPassword())) {
            return new LoginUserDto(logInUser.getId(), logInUser.getName(), logInUser.getEmail());
        }
        throw new LogInException(PASSWORD_FAIL_MESSAGE);
    }
}
