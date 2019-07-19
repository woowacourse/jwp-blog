package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.dto.LogInInfoDto;
import techcourse.myblog.dto.UserPublicInfoDto;
import techcourse.myblog.dto.UserDto;
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

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("name", "email@woowa.com", VALID_PASSWORD, VALID_PASSWORD);
        userRepository.save(userDto.toEntity());
    }

    @Test
    @DisplayName("이메일과 비밀번호를 넘겨받아 로그인에 성공한다.")
    void loginSuccess() {
        LogInInfoDto logInInfoDto = new LogInInfoDto("email@woowa.com", VALID_PASSWORD);

        UserPublicInfoDto userPublicInfoDto = logInService.logIn(logInInfoDto);
        assertThat(userPublicInfoDto.getName()).isEqualTo("name");
        assertThat(userPublicInfoDto.getEmail()).isEqualTo("email@woowa.com");
    }

    @Test
    @DisplayName("이메일이 없는 경우 로그인에 실패한다.")
    void logInFailWhenEmailIsNotInRepository() {
        LogInInfoDto logInInfoDto = new LogInInfoDto("test2@email.com", VALID_PASSWORD);

        assertThatThrownBy(() -> logInService.logIn(logInInfoDto)).isInstanceOf(LogInException.class);
    }

    @Test
    @DisplayName("비밀번호가 다른 경우 로그인에 실패한다.")
    void logInFailWhenDifferentPassword() {
        LogInInfoDto logInInfoDto = new LogInInfoDto("email@woowa.com", VALID_PASSWORD + "diff");

        assertThatThrownBy(() -> logInService.logIn(logInInfoDto)).isInstanceOf(LogInException.class);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}