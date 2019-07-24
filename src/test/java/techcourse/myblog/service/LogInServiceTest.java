package techcourse.myblog.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.LogInInfoDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.exception.LogInException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LogInServiceTest {
    @Autowired
    LogInService logInService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("이메일과 비밀번호를 넘겨받아 로그인에 성공한다.")
    void loginSuccess() {
        LogInInfoDto logInInfoDto = new LogInInfoDto("test@test.test", VALID_PASSWORD);

        UserProfileDto userProfileDto = logInService.logIn(logInInfoDto);
        assertThat(userProfileDto.getName()).isEqualTo("test");
        assertThat(userProfileDto.getEmail()).isEqualTo("test@test.test");
    }

    @Test
    @DisplayName("이메일이 없는 경우 로그인에 실패한다.")
    void logInFailWhenEmailIsNotInRepository() {
        LogInInfoDto logInInfoDto = new LogInInfoDto("test2@test.test", VALID_PASSWORD);

        assertThatThrownBy(() -> logInService.logIn(logInInfoDto)).isInstanceOf(LogInException.class);
    }

    @Test
    @DisplayName("비밀번호가 다른 경우 로그인에 실패한다.")
    void logInFailWhenDifferentPassword() {
        LogInInfoDto logInInfoDto = new LogInInfoDto("test@test.test", VALID_PASSWORD + "diff");

        assertThatThrownBy(() -> logInService.logIn(logInInfoDto)).isInstanceOf(LogInException.class);
    }
}