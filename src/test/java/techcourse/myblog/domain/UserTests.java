package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
	@Test
	void matchPassword() {
		UserDto userDto = new UserDto();
		userDto.setUsername("tiber");
		userDto.setPassword("asdfASDF1@");
		userDto.setEmail("tiber@naver.com");

		User user = new User();
		user.saveUser(userDto);

		assertTrue(user.matchPassword("asdfASDF1@"));
	}
}
