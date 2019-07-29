package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LogInInfoDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.LogInException;

import static techcourse.myblog.service.exception.LogInException.LOGIN_FAIL_MESSAGE;

@Service
public class LogInService {
    private UserRepository userRepository;

    public LogInService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDto logIn(LogInInfoDto logInInfoDto) {
        User logInUser = userRepository.findByEmailAndPassword(logInInfoDto.getEmail(), logInInfoDto.getPassword())
                .orElseThrow(() -> new LogInException(LOGIN_FAIL_MESSAGE));
        return new UserProfileDto(logInUser.getId(), logInUser.getName(), logInUser.getEmail());
    }
}
