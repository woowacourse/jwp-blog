package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.exception.UserArgumentException;
import techcourse.myblog.domain.user.User;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
	private static final String VALID_NAME = "name";
	private static final String VALID_EMAIL = "email@woowa.com";
	private static final String VALID_PASSWORD = "passWORD!1";

	@Test
	public void 이름의_길이가_2자_미만인_경우() {
		assertThatThrownBy(() -> new User("a", VALID_EMAIL, VALID_PASSWORD))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 이름의_길이가_10자_초과인_경우() {
		assertThatThrownBy(() -> new User("abcdefghijk", VALID_EMAIL, VALID_PASSWORD))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 이름이_숫자를_포함하는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME + 1, VALID_EMAIL, VALID_PASSWORD))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 이름이_특수문자를_포함하는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME + "!@", VALID_EMAIL, VALID_PASSWORD))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 비밀번호가_8자_미만인_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME, VALID_EMAIL, "passW!1"))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 비밀번호에_소문자가_없는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME, VALID_EMAIL, "PASSWORD!1"))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 비밀번호에_대문자가_없는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME, VALID_EMAIL, "password!1"))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 비밀번호에_숫자가_없는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME, VALID_EMAIL, "password!"))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 비밀번호에_특수문자가_없는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME, VALID_EMAIL, "password!"))
				.isInstanceOf(UserArgumentException.class);
	}

	@Test
	public void 비밀번호에_한글이_있는_경우() {
		assertThatThrownBy(() -> new User(VALID_NAME, VALID_EMAIL, "가password!1"))
				.isInstanceOf(UserArgumentException.class);
	}
}
