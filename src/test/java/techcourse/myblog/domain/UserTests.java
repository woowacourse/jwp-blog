package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.request.UserDto;
import techcourse.myblog.dto.request.UserEditProfileDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
	@Test
	void saveWithUserDto() {
		UserDto userDto = createUserDto();

		User user = new User();
		user.saveUser(userDto);

		assertThat(user.getUsername()).isEqualTo(userDto.getUsername());
		assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
		assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
	}

	@Test
	void matchPassword() {
		UserDto userDto = createUserDto();

		User user = new User();
		user.saveUser(userDto);

		assertTrue(user.matchPassword("asdfASDF1@"));
	}

	@Test
	void editWithUserEditProfileDto() {
		User user = createUser();

		UserEditProfileDto userEditProfileDto = new UserEditProfileDto();
		userEditProfileDto.setUsername("tiberlee");
		userEditProfileDto.setFacebookURL("www.facebook.com");
		userEditProfileDto.setGithubURL("www.github.com");

		user.editUser(userEditProfileDto);
		assertThat(user.getUsername()).isEqualTo(userEditProfileDto.getUsername());
		assertThat(user.getFacebookURL()).isEqualTo(userEditProfileDto.getFacebookURL());
		assertThat(user.getGithubURL()).isEqualTo(userEditProfileDto.getGithubURL());
	}

	private User createUser() {
		UserDto userDto = createUserDto();

		User user = new User();
		user.saveUser(userDto);

		return user;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setUsername("tiber");
		userDto.setPassword("asdfASDF1@");
		userDto.setEmail("tiber@naver.com");

		return userDto;
	}
}
