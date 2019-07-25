package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.service.exception.LogInException;

import static techcourse.myblog.service.exception.LogInException.LOGIN_FAIL_MESSAGE;

@Service
public class LogInService {
    private UserRepository userRepository;

    public LogInService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPublicInfoDto logIn(LogInInfoDto logInInfoDto) {
        User logInUser = userRepository.findByEmailAndPassword(logInInfoDto.getEmail(), logInInfoDto.getPassword())
                .orElseThrow(() -> new LogInException(LOGIN_FAIL_MESSAGE));
        return new UserPublicInfoDto(logInUser.getId(), logInUser.getName(), logInUser.getEmail());
    }
}
