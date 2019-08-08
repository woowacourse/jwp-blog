package techcourse.myblog.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
	public static final User user = User.builder()
			.userName("heejoo")
			.email("heejoo@gmail.com")
			.password("Aa12345!")
			.build();

	public static final User user2 = User.builder()
			.userName("cony")
			.email("cony@gmail.com")
			.password("Aa12345!")
			.build();

	@Test
	void 로그인_시_패스워드_일치() {
		assertThat(user.matchPassword("Aa12345!")).isTrue();
	}

	@Test
	void 로그인_시_패스워드_불일치() {
		assertThat(user.matchPassword("sdasdasda")).isFalse();
	}

	@Test
	void 유저이름_변경() {
		User user3 = new User("buddy2", "buddy@gmail.com", "Aa12345!");
		user3.changeUserName("ssosso");

		assertThat(user3.getUserName()).isEqualTo("ssosso");
	}
}
