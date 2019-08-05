package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.Information;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.request.UserSignUpInfoDto;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTests {
	@Test
	void update() {
		Information userInfo = new UserSignUpInfoDto("tiber", "tiber@naver.com", "asdfASDF1@")
				.valueOfInfo();
		User user = new User(userInfo);

		Contents actualContents = new Contents("contentText");
		Comment actualComment = new Comment(user, actualContents);
		Contents updateContents = new Contents("updateContentText");
		Comment updateComment = new Comment(user, updateContents);
		actualComment.update(updateContents);
		assertThat(actualComment.getText()).isEqualTo(updateComment.getText());
	}
}