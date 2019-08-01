package techcourse.myblog.domain.vo.user;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserSignUpInfoTests {
	@Test
	void valueOfUser() {
		UserSignUpInfo userSignUpInfo = new UserSignUpInfo("tiber", "asdfASDF1@", "tiber@naver.com");
		User user = userSignUpInfo.valueOfUser();
		assertThat(user.getUsername()).isEqualTo(userSignUpInfo.getUsername());
		assertThat(user.getEmail()).isEqualTo(userSignUpInfo.getEmail());
		assertThat(user.getPassword()).isEqualTo(userSignUpInfo.getPassword());
	}
}