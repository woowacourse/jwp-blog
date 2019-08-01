package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.vo.user.UserSignUpInfo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
	private User user;

	@BeforeEach
	void setUp() {
		user = createUserVo().valueOfUser();
	}

	@Test
	void matchPassword() {
		assertTrue(user.matchPassword("asdfASDF1@"));
	}

	@Test
	void dontMatchPassword() {
		assertFalse(user.matchPassword("dontMatchPassword!"));
	}

	private UserSignUpInfo createUserVo() {
		return new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@");
	}
}
