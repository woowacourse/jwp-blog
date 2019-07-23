package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.web.UserRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginServiceTest {
	private static final String NAME = "yusi";
	private static final String EMAIL = "temp@mail.com";
	private static final String PASSWORD = "12345abc";

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@BeforeEach
	public void setUp() {
		userService.deleteAll();
	}

	@Test
	public void 로그인_가능() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpRequestDto);

		UserRequestDto.LoginRequestDto loginRequestDto = new UserRequestDto.LoginRequestDto(EMAIL, PASSWORD);
		assertEquals(expected, loginService.login(loginRequestDto));
	}

	@Test
	public void 로그인_불가능() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpRequestDto);

		UserRequestDto.LoginRequestDto loginRequestDto = new UserRequestDto.LoginRequestDto(EMAIL, PASSWORD);
		assertEquals(expected, loginService.login(loginRequestDto));
	}
}