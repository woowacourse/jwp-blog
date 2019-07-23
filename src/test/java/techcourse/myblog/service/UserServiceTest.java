package techcourse.myblog.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.web.UserRequestDto;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
		User expected = userService.save(signUpRequestDto);
		User actual = userService.findById(expected.getId());

		assertEquals(expected, actual);
	}

	@Test
	public void 회원_조회() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpRequestDto);

		assertEquals(expected, userService.findById(expected.getId()));
	}

	@Test
	@DisplayName("회원 목록 조회")
	public void findAll() {
		String secondName = NAME + "eun";
		String secondPassword = PASSWORD + "1562";

		UserRequestDto.SignUpRequestDto userDto1 = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		UserRequestDto.SignUpRequestDto userDto2
				= new UserRequestDto.SignUpRequestDto(secondName, EMAIL, secondPassword);
		User user1 = userService.save(userDto1);
		User user2 = userService.save(userDto2);

		Iterable<User> actual = userService.findAll();
		assertThat(actual, Matchers.contains(user1, user2));
	}

	@Test
	public void 해당_회원이_존재하는지_확인() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		userService.save(signUpRequestDto);

		assertTrue(userService.exitsByEmail(signUpRequestDto));
	}

	@Test
	public void 회원_수정() {
		final String modifyName = NAME + "eun";
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User expected = userService.save(signUpRequestDto);
		expected.update(new User(modifyName, EMAIL, PASSWORD));

		UserRequestDto.UpdateRequestDto updateRequestDto
				= new UserRequestDto.UpdateRequestDto(expected.getId(), modifyName, EMAIL);
		userService.update(updateRequestDto);

		User actual = userService.findById(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void 회원_탈퇴() {
		UserRequestDto.SignUpRequestDto signUpRequestDto = new UserRequestDto.SignUpRequestDto(NAME, EMAIL, PASSWORD);
		User user = userService.save(signUpRequestDto);

		userService.deleteByEmail(EMAIL);

		assertThrows(NoSuchElementException.class, () -> {
			userService.findById(user.getId());
		});
	}

}