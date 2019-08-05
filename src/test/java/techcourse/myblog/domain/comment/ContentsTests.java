package techcourse.myblog.domain.comment;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.Information;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.request.UserSignUpInfoDto;

import static org.assertj.core.api.Assertions.assertThat;

class ContentsTests {
	@Test
	void valueOfComment() {
		Information userInfo = new UserSignUpInfoDto("tiber", "tiber@naver.com", "asdfASDF1@")
				.valueOfInfo();
		User user = new User(userInfo);

		Contents contents = new Contents("contentText");
		Comment comment = new Comment(user, contents);
		assertThat(contents.getText()).isEqualTo(comment.getText());
	}

	@Test
	void valueOfCommentWithUser() {
		Information info = new UserSignUpInfoDto("tiber", "tiber@naver.com", "asdfASDF1@")
				.valueOfInfo();
		User user = new User(info);

		Contents contents = new Contents("contentText");
		Comment comment = new Comment(user, contents);
		assertThat(contents.getText()).isEqualTo(comment.getText());
		assertThat(comment.getAuthor().getEmail()).isEqualTo("tiber@naver.com");
	}
}