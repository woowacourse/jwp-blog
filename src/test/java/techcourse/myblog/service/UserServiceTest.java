package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.web.UserRequestDto;

import java.util.NoSuchElementException;

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
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User actual = userService.save(signUpRequestDto);

		User expected = signUpRequestDto.toUser();
		expected.setId(actual.getId());

		assertEquals(expected, actual);
	}

	@Test
	public void 회원_조회_By_ID() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpRequestDto);

		assertEquals(expected, userService.findById(expected.getId()));
	}

	@Test
	public void 해당_회원이_존재하는지_확인() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		userService.save(signUpRequestDto);

		assertTrue(userService.exitsByEmail(signUpRequestDto));
	}

	@Test
	public void 이메일을_기준으로_회원_탈퇴() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		userService.save(signUpRequestDto);

		userService.deleteByEmail(EMAIL);

		UserRequestDto.LoginRequestDto loginRequestDto = new UserRequestDto.LoginRequestDto(EMAIL, PASSWORD);

		assertThrows(NoSuchElementException.class, () -> {
			userService.findByLoginInfo(loginRequestDto);
		});
	}

}