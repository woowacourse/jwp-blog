package techcourse.myblog.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.user.Information;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserChangeableInfo;
import techcourse.myblog.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
	private User user;

	@BeforeEach
	void setUp() {
		Information actualInfo = new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@")
				.valueOfInfo();
		user = new User(actualInfo);
	}

	@Test
	void update() {
		Information updateInfo = new UserChangeableInfo("jason", "www.github.com/jason", "wwww.facebook.com/jason")
				.valueOfInfo();
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
