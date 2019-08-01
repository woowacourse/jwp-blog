package techcourse.myblog.domain.vo.user;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

public class UserBasicInfoTests {
	@Test
	void valueOfUser() {
		UserBasicInfo userBasicInfo = new UserBasicInfo("tiber", "tiber@naver.com");
		User user = userBasicInfo.valueOfUser();
		assertThat(user.getUsername()).isEqualTo(userBasicInfo.getUsername());
		assertThat(user.getEmail()).isEqualTo(userBasicInfo.getEmail());
	}
}
