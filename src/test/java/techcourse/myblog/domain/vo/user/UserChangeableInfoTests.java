package techcourse.myblog.domain.vo.user;

import org.junit.jupiter.api.Test;
import techcourse.myblog.user.User;
import techcourse.myblog.user.UserChangeableInfo;
import techcourse.myblog.user.UserSignUpInfo;

import static org.assertj.core.api.Assertions.assertThat;

class UserChangeableInfoTests {
	@Test
	void editWithUserChangeableInfo() {
		User user = new UserSignUpInfo("tiber", "tiber@naver.com", "asdfASDF1@").valueOfUser();
		UserChangeableInfo userChangeableInfo = new UserChangeableInfo("tiberlee", "tiber@naver.com","www.facebook.com", "www.github.com");
		user.editUser(userChangeableInfo);
		assertThat(user.getUsername()).isEqualTo(userChangeableInfo.getUsername());
		assertThat(user.getEmail()).isEqualTo(userChangeableInfo.getEmail());
		assertThat(user.getFacebookUrl()).isEqualTo(userChangeableInfo.getFaceBookUrl());
		assertThat(user.getGithubUrl()).isEqualTo(userChangeableInfo.getGithubUrl());
	}
}