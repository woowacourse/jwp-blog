package techcourse.myblog.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.service.request.UserChangeableInfoDto;
import techcourse.myblog.service.request.UserSignUpInfoDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
	private User user;

	@BeforeEach
	void setUp() {
		Information actualInfo = new UserSignUpInfoDto("tiber", "tiber@naver.com", "asdfASDF1@")
				.valueOfInfo();
		user = new User(actualInfo);
	}

	@Test
	void update() {
		Information updateInfo = new UserChangeableInfoDto("jason", "www.github.com/jason", "wwww.facebook.com/jason")
				.valueOfInfo(user);
		user.editUser(updateInfo);
		assertThat(user.getUsername()).isEqualTo(updateInfo.getUsername());
		assertThat(user.getGithubUrl()).isEqualTo(updateInfo.getGithubUrl());
		assertThat(user.getFacebookUrl()).isEqualTo(updateInfo.getFacebookUrl());
	}

	@Test
	void matchPassword() {
		assertTrue(user.matchPassword("asdfASDF1@"));
	}

	@Test
	void dontMatchPassword() {
		assertFalse(user.matchPassword("dontMatchPassword!"));
	}
}
