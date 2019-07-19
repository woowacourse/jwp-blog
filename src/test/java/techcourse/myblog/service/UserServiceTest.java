package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserServiceTest {
	private static final String NAME = "yusi";
	private static final String EMAIL = "temp@mail.com";
	private static final String PASSWORD = "12345abc";

	@Autowired
	UserService userService;

	@BeforeEach
	public void setUp() {
		userService.deleteAll();
	}

	@Test
	public void 회원_추가() {
		UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
		User actual = userService.save(signUpUserInfo);

		User expected = signUpUserInfo.toUser();
		expected.setId(actual.getId());

		assertEquals(expected, actual);
	}

	@Test
	public void 회원_조회_By_ID() {
		UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpUserInfo);

		assertEquals(expected, userService.findById(expected.getId()));
	}

	@Test
	public void 회원_조회_By_LoginInfo() {
		UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpUserInfo);
		UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(EMAIL, PASSWORD);

		assertEquals(expected, userService.findByLoginInfo(loginInfo));
	}

	@Test
	public void 해당_회원이_존재하는지_확인() {
		UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
		userService.save(signUpUserInfo);

		assertTrue(userService.exitsByEmail(signUpUserInfo));
	}

	@Test
	public void 로그인_가능() {
		UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
		userService.save(signUpUserInfo);

		UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(EMAIL, PASSWORD);

		assertTrue(userService.canLogin(loginInfo));
	}

	@Test
	public void 로그인_불가능() {
		UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(EMAIL, PASSWORD);
		assertFalse(userService.canLogin(loginInfo));
	}

	@Test
	public void 이메일을_기준으로_삭제() {
		UserDto.SignUpUserInfo signUpUserInfo = new UserDto.SignUpUserInfo(NAME, EMAIL, PASSWORD);
		userService.save(signUpUserInfo);

		userService.deleteByEmail(EMAIL);
	}

}