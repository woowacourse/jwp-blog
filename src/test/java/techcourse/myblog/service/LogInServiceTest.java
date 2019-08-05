package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.user.UserRepository;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.service.exception.LogInException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static techcourse.myblog.service.UserServiceTest.VALID_PASSWORD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogInServiceTest {
	private User user;

	@Autowired
	LogInService logInService;

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	public void setUp() {
		user = new User("name", "login1@woowa.com", VALID_PASSWORD);
		userRepository.save(user);
	}

	@Test
	@DisplayName("이메일과 비밀번호를 넘겨받아 로그인에 성공한다.")
	public void loginSuccess() {
		LogInInfoDto logInInfoDto = new LogInInfoDto("login1@woowa.com", VALID_PASSWORD);

		UserSessionDto userSessionDto = logInService.logIn(logInInfoDto);
		assertThat(userSessionDto.getName()).isEqualTo("name");
		assertThat(userSessionDto.getEmail()).isEqualTo("login1@woowa.com");
	}

	@Test
	@DisplayName("이메일이 없는 경우 로그인에 실패한다.")
	public void logInFailWhenEmailIsNotInRepository() {
		LogInInfoDto logInInfoDto = new LogInInfoDto("test2@email.com", VALID_PASSWORD);

		assertThatThrownBy(() -> logInService.logIn(logInInfoDto)).isInstanceOf(LogInException.class);
	}

	@Test
	@DisplayName("비밀번호가 다른 경우 로그인에 실패한다.")
	public void logInFailWhenDifferentPassword() {
		LogInInfoDto logInInfoDto = new LogInInfoDto("login1@woowa.com", VALID_PASSWORD + "diff");

		assertThatThrownBy(() -> logInService.logIn(logInInfoDto)).isInstanceOf(LogInException.class);
	}

	@AfterEach
	public void tearDown() {
		userRepository.delete(user);
	}
}